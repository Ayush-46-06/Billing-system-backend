package com.athenura.billing_system.controller;

import com.athenura.billing_system.user.dto.CreateUserRequest;
import com.athenura.billing_system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @PostMapping("/users")
    public String createUser(@RequestBody CreateUserRequest request){
        userService.createUser(request);
        return "user created successfully";
    }
}
