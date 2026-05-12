package com.university.sfms.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.university.sfms.dto.AuthDtos.*;
import com.university.sfms.repository.UserRepository;
import com.university.sfms.security.JwtService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authManager;
    private final UserDetailsService userDetailsService;
    private final UserRepository users;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authManager, UserDetailsService userDetailsService, UserRepository users, JwtService jwtService) {
        this.authManager = authManager;
        this.userDetailsService = userDetailsService;
        this.users = users;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        var user = users.findByEmail(request.email()).orElseThrow();
        var details = userDetailsService.loadUserByUsername(request.email());
        String token = jwtService.generateToken(details, Map.of("role", user.getRole().getName().name(), "userId", user.getUserId()));
        return ResponseEntity.ok(new LoginResponse(token, "Bearer", user.getUserId(), user.getName(), user.getEmail(), user.getRole().getName()));
    }
}
