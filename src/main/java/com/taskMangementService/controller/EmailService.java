package com.taskMangementService.controller;

import com.taskMangementService.model.dto.EmailRequest;
import com.taskMangementService.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/email")
public class EmailController {
    private final EmailService emailService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private ResponseEntity<String> sendEmail(@RequestBody EmailRequest request) {
        emailService.sendSimpleMessage(request);
        return ResponseEntity.ok("Email sent successfully");
    }

}
