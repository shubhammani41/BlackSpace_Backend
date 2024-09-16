package com.dev.blackspace.services.impl;

import com.dev.blackspace.entities.UserLoginEntity;
import com.dev.blackspace.entities.UserProfileEntity;
import com.dev.blackspace.repositories.UserLoginRepo;
import com.dev.blackspace.repositories.UserProfileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserLoginRepo userLoginRepo;

    @Override
    public CustomUserDetails loadUserByUsername(String phoneOrEmail) throws IllegalArgumentException {
        UserLoginEntity user = userLoginRepo.findByPhoneOrEmail(phoneOrEmail);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomUserDetails(user);
    }
}
