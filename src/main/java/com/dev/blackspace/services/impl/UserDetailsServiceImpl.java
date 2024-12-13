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
    @Autowired
    private UserProfileRepo userProfileRepo;
    private UserProfileEntity userProfile;
    private UserLoginEntity userLoginData;

    @Override
    public CustomUserDetails loadUserByUsername(String phoneOrEmail) throws IllegalArgumentException {
        this.userLoginData = userLoginRepo.findByPhoneOrEmail(phoneOrEmail);
        if (userLoginData == null) {
            throw new UsernameNotFoundException("User not found");
        }
        this.userProfile = this.userProfileRepo.findByUserId(userLoginData.getUserProfileId());
        return new CustomUserDetails(userLoginData,userProfile);
    }
}
