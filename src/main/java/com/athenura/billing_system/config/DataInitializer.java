package com.athenura.billing_system.config;

import com.athenura.billing_system.repository.UserRepository;
import com.athenura.billing_system.user.Role;
import com.athenura.billing_system.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if (userRepository.findByEmail("admin@athenura.com").isEmpty()) {

            User admin = User.builder()
                    .name("Super Admin")
                    .email("admin@athenura.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(admin);
        }
    }
}
