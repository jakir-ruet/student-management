package com.jakirbd.student_management.auth.controller;

import com.jakirbd.student_management.auth.dto.AuthResponse;
import com.jakirbd.student_management.auth.dto.ChangePasswordRequest;
import com.jakirbd.student_management.auth.dto.LoginRequest;
import com.jakirbd.student_management.auth.dto.RegisterRequest;
import com.jakirbd.student_management.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Long> register(@Valid @RequestBody RegisterRequest request) {
        Long userId = authService.register(request);
        return ResponseEntity.ok(userId);
    }

    @PostMapping("/login")
    public ResponseEntity <AuthResponse> login (@RequestBody LoginRequest request,
                                                HttpServletRequest httpRequest) {
        String ipAddress = httpRequest.getRemoteAddr();
        AuthResponse response = authService.login(request, ipAddress);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request) {
        authService.changePassword(request);
        return ResponseEntity.ok("Password changed successfully");
    }

    @PostMapping("/lock/{userId}")
    public ResponseEntity<String> lockUser(@PathVariable Long userId) {
        authService.lockUser(userId);
        return ResponseEntity.ok("User locked successfully");
    }

    @PostMapping("/unlock/{userId}")
    public ResponseEntity<String> unlockUser(@PathVariable Long userId) {
        authService.unlockUser(userId);
        return ResponseEntity.ok("User unlocked successfully");
    }
}
