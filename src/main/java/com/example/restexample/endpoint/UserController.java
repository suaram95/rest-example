package com.example.restexample.endpoint;

import com.example.restexample.dto.AuthRequest;
import com.example.restexample.dto.AuthResponse;
import com.example.restexample.dto.UserDto;
import com.example.restexample.exception.DuplicateEntityException;
import com.example.restexample.exception.ResourceNotFoundException;
import com.example.restexample.model.User;
import com.example.restexample.service.SMSService;
import com.example.restexample.service.TwilioSMSService;
import com.example.restexample.service.UserService;
import com.example.restexample.util.JWTTokenUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final JWTTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final TwilioSMSService smsService;

    @PostMapping("/user/auth")
    public ResponseEntity auth(@RequestBody AuthRequest authRequest) {
        Optional<User> userByEmail = userService.findByEmail(authRequest.getEmail());
        if (userByEmail.isPresent()) {
            User user = userByEmail.get();
            if (passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
                String token = jwtTokenUtil.generateToken(user.getEmail());
                return ResponseEntity.ok(AuthResponse.builder()
                        .token(token)
                        .name(user.getName())
                        .surname(user.getSurname())
                        .build());
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("unauthorized");
    }

    @PostMapping("/user")
    public User save(@RequestBody UserDto userDto) {
        if (!userService.findByEmail(userDto.getEmail()).isPresent()) {
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
            User user = modelMapper.map(userDto, User.class);
            User userSave = userService.save(user);
            if (userDto.getPhoneNumber() != null && !userDto.getPhoneNumber().isEmpty()) {
                smsService.send(userDto.getPhoneNumber(), "Hello! " + userDto.getName());
            }
            return userSave;
        } else {
            throw new DuplicateEntityException("username " + userDto.getEmail() + " already exists");
        }
    }

    @PutMapping("/user/{userId}/image")
    public void uploadImage(@PathVariable("userId") int userId,
                            @RequestParam("image") MultipartFile file) {
        Optional<User> userById = userService.findById(userId);
        if (!userById.isPresent()) {
            throw new ResourceNotFoundException("User was not found");
        }
        System.out.println(file.getOriginalFilename());
    }
}
