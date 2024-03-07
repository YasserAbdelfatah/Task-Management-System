package com.taskMangementService.service;


import com.taskMangementService.error.exceptions.InvalidDataException;
import com.taskMangementService.error.exceptions.UserAlreadyExistException;
import com.taskMangementService.error.exceptions.UserNotFoundException;
import com.taskMangementService.model.dto.UserDto;
import com.taskMangementService.model.entities.User;
import com.taskMangementService.model.response.LoginResponse;
import com.taskMangementService.repositories.UserRepository;
import com.taskMangementService.utils.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Transactional
    public void register(UserDto request) {
        if (request.isInValidData()) {
            throw new InvalidDataException("Invalid data provided!");
        }

        if (userRepository.existsByUserName(request.getUserName())) {
            throw new UserAlreadyExistException("User already exists!");
        }

        userRepository.save(User.builder()
                .userName(request.getUserName())
                .password(passwordEncoder.encode(request.getPassword()))
                .build());
    }

    public LoginResponse authenticate(UserDto request) {
        if (request.isInValidData()) {
            throw new InvalidDataException("Invalid data provided!");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword()));

        User user = userRepository.findByUserName(request.getUserName()).orElseThrow(() -> new UserNotFoundException(request.getUserName()));

        String jwtToken = jwtUtil.createJWTToken(user.getUserName(), user.getRole().name());

        return LoginResponse.builder().userName(user.getUserName())
                .loginStatus(LoginResponse.LoginStatus.AUTHORIZED)
                .token(jwtToken).userRole(user.getRole()).build();
    }
}
