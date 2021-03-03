package com.example.restexample.endpoint;

import com.example.restexample.dto.ToDo;
import com.example.restexample.exception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class ToDoController {

    String baseUrl = "https://jsonplaceholder.typicode.com/todos";

    @GetMapping("/todos")
    public List<ToDo> todos() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ToDo[]> response = restTemplate.getForEntity(baseUrl, ToDo[].class);
        ToDo[] body = response.getBody();
        if (body == null) {
            return new ArrayList<>();
        }
        return Arrays.asList(body);
    }

    @GetMapping("/todos/{id}")
    public ToDo getTodo(@PathVariable("id") int id) {
        RestTemplate template = new RestTemplate();
        ResponseEntity<ToDo> toDoResponse = template.getForEntity(baseUrl + "/" + id, ToDo.class);
        if (toDoResponse.getBody() != null) {
            return toDoResponse.getBody();
        }
        throw new ResourceNotFoundException("Todo was not found");
    }
}
