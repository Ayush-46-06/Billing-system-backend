package com.athenura.billing_system.user.dto;

public record CreateUserRequest(

        String name,
        String email,
        String password,
        String role,
        String secretKey

) {}