package com.dev.blackspace.repositories;

import com.dev.blackspace.entities.UserExperienceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserExperienceRepo extends JpaRepository<UserExperienceEntity, Integer> {

    Optional<List<UserExperienceEntity>> findByUserIdOrderByFromDateDesc(int userId);
}
