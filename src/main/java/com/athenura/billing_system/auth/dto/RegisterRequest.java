package com.athenura.billing_system.auth.dto;

public record RegisterRequest(
        String name,
        String email,
        String password,
        String role,
        String secretKey
) {}