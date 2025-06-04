package com.alkaid.emailservice_re.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alkaid.emailservice_re.service.impl.EmailServiceImpl;


@RestController
public class EmailController {
    
    @Autowired
    private EmailServiceImpl emailService;


    @PostMapping("/send")
    public String sendEmail() {
        
        return "Success";
    }
}
