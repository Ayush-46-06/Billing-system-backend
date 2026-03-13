package com.athenura.billing_system.service;

import com.athenura.billing_system.user.dto.CreateUserRequest;
import com.athenura.billing_system.repository.UserRepository;
import com.athenura.billing_system.user.Role;
import com.athenura.billing_system.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public void createUser(CreateUserRequest request){

        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.valueOf(request.role()))
                .secretKey(request.secretKey())
                .build();

        userRepository.save(user);
    }
}
