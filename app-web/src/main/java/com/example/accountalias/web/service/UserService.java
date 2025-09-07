package com.example.accountalias.web.service;

import com.example.accountalias.domain.User;
import com.example.accountalias.repository.UserRepository;
import com.example.accountalias.web.dto.AuthDtos;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public AuthDtos.UserResponse register(AuthDtos.RegisterRequest request) {
        Optional<User> existing = userRepository.findByEmail(request.email());
        if (existing.isPresent()) {
            throw new IllegalStateException("User already registered");
        }
        User user = new User();
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        User saved = userRepository.save(user);
        return new AuthDtos.UserResponse(saved.getId(), saved.getEmail());
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public AuthDtos.UserResponse updateEmail(Long userId, String newEmail) {
        User user = userRepository.findById(userId).orElseThrow();
        if (userRepository.existsByEmail(newEmail) && !newEmail.equals(user.getEmail())) {
            throw new IllegalStateException("Email already in use");
        }
        user.setEmail(newEmail);
        return new AuthDtos.UserResponse(user.getId(), user.getEmail());
    }
}

