package com.skillforge.backend.controller;

import com.skillforge.backend.entity.User;
import com.skillforge.backend.service.AuthService;
import com.skillforge.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(UserService userService, AuthService authService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        try {
            User user = new User(
                    request.get("fullName"),
                    passwordEncoder.encode(request.get("password")),
                    request.get("email"),
                    User.UserRole.valueOf(request.get("role").toUpperCase())
            );
            userService.save(user);
            return ResponseEntity.ok(Map.of("message", "User registered", "id", user.getId().toString()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        try {
            String token = authService.login(
                    request.get("email"),
                    request.get("password")
            );
            return ResponseEntity.ok(Map.of("token", token));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}