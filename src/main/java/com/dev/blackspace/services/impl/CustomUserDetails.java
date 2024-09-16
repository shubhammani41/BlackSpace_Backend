package com.dev.blackspace.services.impl;

import com.dev.blackspace.entities.UserLoginEntity;
import com.dev.blackspace.entities.UserProfileEntity;
import com.dev.blackspace.repositories.UserProfileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements org.springframework.security.core.userdetails.UserDetails {
    private UserLoginEntity user;
    private UserProfileEntity userProfile;
    @Autowired
    private UserProfileRepo userProfileRepo;
    @Autowired
    private RoleServiceImpl roleService;

    public CustomUserDetails(UserLoginEntity user) {
        this.user = user;
        this.userProfile = this.userProfileRepo.findByUserId(user.getUserProfileId());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
            if(userProfile!=null){
                Integer roleId = this.userProfileRepo.findByUserId(user.getUserProfileId()).getRoleId();
                String role = roleService.getAuthorityNameById(roleId);
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
                return Collections.singletonList(authority);
            }
            return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        if(userProfile!=null){
           return userProfile.getUserName();
        }
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        if(userProfile!=null){
            return !userProfile.getIsDeactivated();
        }
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
