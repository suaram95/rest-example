package com.example.restexample.service;


import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwilioSMSService implements SMSService {

    @Value("${twilio.accountSid}")
    private String accountSId;
    @Value("${twilio.token}")
    private String token;

    @Override
    public void send(String phoneNumber, String message) {
        Twilio.init(accountSId, token);

        Message messageObj = Message.creator(new PhoneNumber(phoneNumber),
                new PhoneNumber("+19852759191"), message).create();
        System.out.println(messageObj.getSid());
    }
}
