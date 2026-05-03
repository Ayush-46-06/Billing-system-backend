package com.athenura.billing_system.user.repository;

import com.athenura.billing_system.user.User;
import com.athenura.billing_system.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByRole(Role role);
    Optional<User> findByEmail(String email);
    Optional<User> findByResetToken(String resetToken);
}


