package com.example.libra.controller;

import com.example.libra.dto.AuthResponse;
import com.example.libra.dto.LoginRequest;
import com.example.libra.dto.RegisterRequest;
import com.example.libra.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        String token = authService.register(
            request.getUsername(),
            request.getEmail(),
            request.getPassword(),
            request.getRole(),
            request.getMemberId()
        );
        return ResponseEntity.ok(new AuthResponse(token, request.getUsername(), request.getRole().name()));
    }
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        String token = authService.login(request.getUsername(), request.getPassword());
        // Get role from token or user
        return ResponseEntity.ok(new AuthResponse(token, request.getUsername(), "MEMBER"));
    }
}