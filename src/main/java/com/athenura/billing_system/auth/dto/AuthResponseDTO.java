package com.athenura.billing_system.auth.dto;

public record AuthResponseDTO(
        String token,
        String name,
        String email,
        String role
) {}