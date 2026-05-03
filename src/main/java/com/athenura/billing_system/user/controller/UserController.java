package com.athenura.billing_system.user.controller;

import com.athenura.billing_system.user.User;
import com.athenura.billing_system.user.repository.UserRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final Cloudinary cloudinary;

    @PostMapping("/upload-profile")
    public ResponseEntity<?> uploadProfileImage(
            @RequestParam("file") MultipartFile file,
            Principal principal
    ) throws IOException {

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Map uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                        "folder", "profile_images",
                        "resource_type", "image",
                        "public_id", "user_" + user.getId(),
                        "overwrite", true
                )
        );

        String imageUrl = uploadResult.get("secure_url").toString();
        user.setProfileImage(imageUrl);
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("url", imageUrl));
    }

    @GetMapping("/me")
    public User getCurrentUser(Principal principal) {
        return userRepository.findByEmail(principal.getName())
                .orElseThrow();
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUser(
            @RequestBody Map<String, String> body,
            Principal principal
    ) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        System.out.println("Incoming body: " + body);

        if (body.containsKey("name") && body.get("name") != null && !body.get("name").trim().isEmpty()) {
            user.setName(body.get("name").trim());
        }

        if (body.containsKey("email") && body.get("email") != null) {
            String newEmail = body.get("email");

            if (!newEmail.equals(user.getEmail()) &&
                    userRepository.findByEmail(newEmail).isPresent()) {
                return ResponseEntity.badRequest().build();
            }

            user.setEmail(newEmail);
        }

        if (body.containsKey("department") && body.get("department") != null && !body.get("department").trim().isEmpty()) {
            user.setDepartment(body.get("department").trim());
        }

        userRepository.save(user);

        return ResponseEntity.ok(user);
    }
}