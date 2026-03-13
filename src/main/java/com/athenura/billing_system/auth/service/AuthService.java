package com.athenura.billing_system.auth.service;

import com.athenura.billing_system.auth.dto.LoginRequest;
import com.athenura.billing_system.repository.UserRepository;
import com.athenura.billing_system.security.jwt.JwtUtil;
import com.athenura.billing_system.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public String login(LoginRequest request) {

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        if (user.getSecretKey() == null ||
                !user.getSecretKey().equals(request.secretKey())) {
            throw new RuntimeException("Invalid secret key");
        }

        return jwtUtil.generateToken(user.getEmail());
    }
}