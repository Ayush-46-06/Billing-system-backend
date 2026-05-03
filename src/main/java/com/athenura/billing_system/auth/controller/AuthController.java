package com.athenura.billing_system.auth.controller;

import com.athenura.billing_system.auth.dto.LoginRequest;
import com.athenura.billing_system.auth.dto.RegisterRequest;
import com.athenura.billing_system.auth.service.AuthService;
import com.athenura.billing_system.user.serviceLayer.PasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final PasswordService passwordService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {

        passwordService.processForgotPassword(email);
        return ResponseEntity.ok(Map.of("message", "Reset link sent successfully."));
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestParam String email,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        passwordService.changePassword(email, oldPassword, newPassword);
        return ResponseEntity.ok(Map.of("message", "Password updated successfully."));
    }


    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        passwordService.updatePasswordWithToken(token, newPassword);
        return ResponseEntity.ok(Map.of("message", "Password has been reset successfully."));
    }
}