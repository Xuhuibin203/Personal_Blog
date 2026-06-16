package com.example.personal_blog.controller;

import com.example.personal_blog.common.ApiResponse;
import com.example.personal_blog.model.dto.LoginRequest;
import com.example.personal_blog.model.dto.RegisterRequest;
import com.example.personal_blog.model.entity.User;
import com.example.personal_blog.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ApiResponse<?> login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/register")
    public ApiResponse<?> register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @GetMapping("/me")
    public ApiResponse<?> me(@AuthenticationPrincipal User user) {
        return authService.getMe(user);
    }
}
