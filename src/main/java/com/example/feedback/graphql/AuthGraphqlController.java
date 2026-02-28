package com.example.feedback.graphql;

import com.example.feedback.dto.auth.AuthResponse;
import com.example.feedback.dto.auth.LoginRequest;
import com.example.feedback.dto.auth.RegisterRequest;
import com.example.feedback.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class AuthGraphqlController {

    private final AuthService authService;

    public AuthGraphqlController(AuthService authService) {
        this.authService = authService;
    }

    @MutationMapping
    public AuthResponse register(@Valid RegisterRequest input) {
        return authService.register(input);
    }

    @MutationMapping
    public AuthResponse login(@Valid LoginRequest input) {
        return authService.login(input);
    }
}

