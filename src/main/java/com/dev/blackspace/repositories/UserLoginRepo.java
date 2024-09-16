package com.dev.blackspace.repositories;

import com.dev.blackspace.entities.UserLoginEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserLoginRepo extends JpaRepository<UserLoginEntity, Long> {
    UserLoginEntity findByUserId(Long userId);
    UserLoginEntity findByPhoneNumber(String phoneNumber);
    UserLoginEntity findByEmail(String email);

    @Query("SELECT u FROM UserLoginEntity u WHERE u.phoneNumber = :param OR u.email = :param")
    UserLoginEntity findByPhoneOrEmail(@Param("param") String phoneOrEmail);
}
