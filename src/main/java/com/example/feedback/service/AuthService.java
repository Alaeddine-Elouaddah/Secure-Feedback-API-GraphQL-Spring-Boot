package com.example.feedback.service;

import com.example.feedback.dto.auth.AuthResponse;
import com.example.feedback.dto.auth.LoginRequest;
import com.example.feedback.dto.auth.RegisterRequest;
import com.example.feedback.mapper.FeedbackMapper;
import com.example.feedback.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserService userService,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        var user = userService.registerUser(request.getUsername(), request.getEmail(), request.getPassword());
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRoles().stream().map(Enum::name).toArray(String[]::new))
                .build();
        String token = jwtService.generateToken(userDetails);
        return new AuthResponse(token, FeedbackMapper.toUserDto(user));
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(principal);
        var user = userService.findByUsername(principal.getUsername());
        return new AuthResponse(token, FeedbackMapper.toUserDto(user));
    }
}

