package com.dev.blackspace.repositories;

import com.dev.blackspace.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<RoleEntity, Integer> {
}
