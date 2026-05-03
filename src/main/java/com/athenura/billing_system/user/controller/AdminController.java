package com.athenura.billing_system.user.controller;

import com.athenura.billing_system.user.User;
import com.athenura.billing_system.user.serviceLayer.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping("/managers")
    public List<User> getManagers() {
        return userService.getAllManagers();
    }

    @DeleteMapping("/managers/{id}")
    public void deleteManager(@PathVariable Long id) {
        userService.deleteManager(id);
    }
}