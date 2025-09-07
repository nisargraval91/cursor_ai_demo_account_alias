package com.example.accountalias.web.controller;

import com.example.accountalias.domain.User;
import com.example.accountalias.web.dto.AuthDtos;
import com.example.accountalias.web.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Email;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get current user profile")
    @GetMapping("/me")
    public ResponseEntity<AuthDtos.UserResponse> me(@AuthenticationPrincipal UserDetails principal) {
        User user = userService.findByEmail(principal.getUsername()).orElseThrow();
        return ResponseEntity.ok(new AuthDtos.UserResponse(user.getId(), user.getEmail()));
    }

    @Operation(summary = "Update current user email")
    @PutMapping("/me/email")
    public ResponseEntity<AuthDtos.UserResponse> updateEmail(@AuthenticationPrincipal UserDetails principal,
                                                             @RequestParam @Email String email) {
        User user = userService.findByEmail(principal.getUsername()).orElseThrow();
        AuthDtos.UserResponse updated = userService.updateEmail(user.getId(), email);
        return ResponseEntity.ok(updated);
    }
}

