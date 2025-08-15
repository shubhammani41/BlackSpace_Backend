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
    private UserProfileEntity userProfile;
    private UserLoginEntity userLoginData;

    public CustomUserDetails(UserLoginEntity userLoginData,UserProfileEntity userProfile) {
        this.userLoginData = userLoginData;
        this.userProfile = userProfile;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
            if(userProfile!=null){
                Integer roleId = userProfile.getRoleId();
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(String.valueOf(roleId));
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

    public String getUserEmailOrPhone(){
        if(userLoginData!=null){
            return userLoginData.getEmail()!=null?userLoginData.getEmail():userLoginData.getPhoneNumber();
        }
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        if(userLoginData!=null){
            return !userLoginData.getIsDeactivated();
        }
        return false;
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
