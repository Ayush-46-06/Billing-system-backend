package com.athenura.billing_system.user.serviceLayer;

public interface PasswordService {

    void processForgotPassword(String email);
    void updatePasswordWithToken(String token, String newPassword);
    void changePassword(String email, String currentPassword, String newPassword);
}

