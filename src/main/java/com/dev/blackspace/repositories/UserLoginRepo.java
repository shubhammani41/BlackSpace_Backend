package com.dev.blackspace.repositories;

import com.dev.blackspace.entities.UserLoginEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLoginRepo extends JpaRepository<UserLoginEntity, Long> {
    UserLoginEntity findByUserId(Long userId);
    UserLoginEntity findByPhoneNumber(String phoneNumber);
    UserLoginEntity findByEmail(String email);
}
