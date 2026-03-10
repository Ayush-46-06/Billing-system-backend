package com.athenura.billing_system.dto;

import com.athenura.billing_system.user.Role;
import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
    private String secretKey;
    private Role role;
}
