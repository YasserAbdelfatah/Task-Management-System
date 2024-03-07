package com.taskMangementService.controller;


import com.taskMangementService.model.dto.UserDto;
import com.taskMangementService.model.response.LoginResponse;
import com.taskMangementService.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    private ResponseEntity<String> registerUser(
            @RequestBody UserDto userDto) {

        userService.register(userDto);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/authenticate")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody @NonNull UserDto userDto) {
        log.info("Authentication request for user {} received!", userDto.getUserName());
        return ResponseEntity.ok(userService.authenticate(userDto));
    }
}
