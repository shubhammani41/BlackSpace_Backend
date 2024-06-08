package com.dev.blackspace.services.impl;

import com.dev.blackspace.entities.RoleEntity;
import com.dev.blackspace.repositories.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl {
    @Autowired
    private RoleRepo roleRepo;

    public String getAuthorityNameById(Integer roleId) {
        Optional<RoleEntity> roleOptional = roleRepo.findById(roleId);

        if (roleOptional.isPresent()) {
            RoleEntity role = roleOptional.get();
            return role.getRoleName();
        } else {
            throw new IllegalArgumentException("Role not found for id: " + roleId);
        }
    }
}
