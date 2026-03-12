package com.athenura.billing_system.client.controller;

import com.athenura.billing_system.config.SecurityConfigProperties;
import com.athenura.billing_system.dto.AuthRequest;
import com.athenura.billing_system.security.jwt.JwtUtil;
import com.athenura.billing_system.user.Role;
import com.athenura.billing_system.user.User;
import com.athenura.billing_system.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController{

    private final AuthenticationManager authenticationManager;
    private final SecurityConfigProperties securityConfigProperties;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public String login(@RequestBody AuthRequest request) {

        // 1️⃣ User fetch from database
        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2️⃣ Role mismatch check
        if (!user.getRole().equals(request.getRole())) {
            throw new RuntimeException("Role mismatch for this user");
        }

        // 3️⃣ Secret key validation
        if (request.getRole() == Role.ADMIN) {

            if (!request.getSecretKey().equals(securityConfigProperties.getAdminSecret())) {
                throw new RuntimeException("Invalid Admin Secret Key");
            }

        } else if (request.getRole() == Role.MANAGER) {

            if (!request.getSecretKey().equals(securityConfigProperties.getManagerSecret())) {
                throw new RuntimeException("Invalid Manager Secret Key");
            }

        }

        // 4️⃣ Spring Security authentication
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // 5️⃣ Generate JWT
        return jwtUtil.generateToken(request.getEmail());
    }
}