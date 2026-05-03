package com.athenura.billing_system.user.serviceLayerImp;

import com.athenura.billing_system.user.Role;
import com.athenura.billing_system.user.User;
import com.athenura.billing_system.user.repository.UserRepository;
import com.athenura.billing_system.user.serviceLayer.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> getAllManagers() {
        return userRepository.findByRole(Role.MANAGER);
    }

    @Override
    public void deleteManager(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != Role.MANAGER) {
            throw new RuntimeException("Not a manager");
        }

        userRepository.deleteById(id);
    }


}