package com.athenura.billing_system.user.serviceLayer;

import com.athenura.billing_system.user.User;

import java.util.List;

public interface UserService {

    List<User> getAllManagers();

    void deleteManager(Long id);
}