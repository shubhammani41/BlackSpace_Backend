package com.dev.blackspace.services.impl;

import com.dev.blackspace.entities.UserProfileEntity;
import com.dev.blackspace.repositories.UserProfileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserProfileRepo userProfileRepo;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws IllegalArgumentException {
        UserProfileEntity user = userProfileRepo.findByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomUserDetails(user);
    }
}
