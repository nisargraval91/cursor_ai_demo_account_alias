package com.example.accountalias.web.controller;

import com.example.accountalias.domain.User;
import com.example.accountalias.web.dto.AuthDtos;
import com.example.accountalias.web.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth")
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @Operation(summary = "Register a new user")
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid AuthDtos.RegisterRequest request) {
        try {
            AuthDtos.UserResponse user = userService.register(request);
            return ResponseEntity.created(URI.create("/api/users/me")).body(user);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(Map.of(
                    "message", "User already registered",
                    "next", "/api/auth/login"
            ));
        }
    }

    @Operation(summary = "Login existing user")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthDtos.LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return ResponseEntity.ok(Map.of("message", "Login successful"));
        } catch (Exception ex) {
            return ResponseEntity.status(401).body(Map.of("message", "Invalid email or password"));
        }
    }
}

