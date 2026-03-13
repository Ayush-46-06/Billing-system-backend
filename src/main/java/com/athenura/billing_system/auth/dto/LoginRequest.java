package com.athenura.billing_system.auth.dto;

public record LoginRequest(

        String email,
        String password,
        String secretKey
) {}