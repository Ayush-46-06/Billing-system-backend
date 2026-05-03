package com.athenura.billing_system.auth.service;

import com.athenura.billing_system.auth.dto.AuthResponseDTO;
import com.athenura.billing_system.auth.dto.LoginRequest;
import com.athenura.billing_system.auth.dto.RegisterRequest;
import com.athenura.billing_system.security.jwt.JwtUtil;
import com.athenura.billing_system.user.Role;
import com.athenura.billing_system.user.User;
import com.athenura.billing_system.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    @Value("${spring.security.admin-secret}")
    private String adminSecret;

    @Value("${spring.security.manager-secret}")
    private String managerSecret;


    public AuthResponseDTO login(LoginRequest request) {

        System.out.println("LOGIN EMAIL: " + request.email());

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("User not found"));

        System.out.println("USER FOUND: " + user.getEmail());

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            System.out.println("PASSWORD MISMATCH!");
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        return new AuthResponseDTO(
                token,
                user.getName(),
                user.getEmail(),
                user.getRole().name()
        );
    }


    public String register(RegisterRequest request) {

        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        if (request.role().equalsIgnoreCase("ADMIN")) {

            if (!adminSecret.equals(request.secretKey())) {
                throw new RuntimeException("Invalid ADMIN secret key");
            }

        } else if (request.role().equalsIgnoreCase("MANAGER")) {

            if (!managerSecret.equals(request.secretKey())) {
                throw new RuntimeException("Invalid MANAGER secret key");
            }

        } else {
            throw new RuntimeException("Invalid role");
        }

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.valueOf(request.role()));

        userRepository.save(user);

        return "Registration successful";
    }


}