package com.dev.blackspace.services.impl;

import com.dev.blackspace.DTOs.PaginationDTO;
import com.dev.blackspace.DTOs.UserDetailsProj;
import com.dev.blackspace.entities.UserExperienceEntity;
import com.dev.blackspace.entities.UserProfileEntity;
import com.dev.blackspace.repositories.UserExperienceRepo;
import com.dev.blackspace.repositories.UserProfileRepo;
import com.dev.blackspace.utils.StringUtil;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl {

    @Autowired
    private UserProfileRepo userRepo;

    @Autowired
    private UserExperienceRepo userExpRepo;

    @Autowired
    private StringUtil stringUtil;

    public PaginationDTO<List<UserDetailsProj>> getRandomUserListByPage(Pageable pageable) {
        Page<UserDetailsProj> pageData = this.userRepo.findUserDetailsByRandomAndPage(pageable);
        if (pageData != null) {
            return PaginationDTO.<List<UserDetailsProj>>builder().pageSize(pageData.getSize())
                    .totalPages(pageData.getTotalPages()).totalElements(pageData.getTotalElements()).data(pageData.getContent()).build();
        }

        return PaginationDTO.<List<UserDetailsProj>>builder().pageSize(0)
                .totalPages(0).totalElements(0L).data(Collections.emptyList()).build();

    }

    public PaginationDTO<List<UserDetailsProj>> searchUsersByKeyword(Pageable pageable, String searchKeyWord) {
        String searchRegex = this.stringUtil.getSearchRegex(searchKeyWord);
        Page<UserDetailsProj> pageData = this.userRepo.findUserDetailsBySearchKeyWord(pageable, searchRegex);
        if (pageData != null) {
            return PaginationDTO.<List<UserDetailsProj>>builder().pageSize(pageData.getSize())
                    .totalPages(pageData.getTotalPages()).totalElements(pageData.getTotalElements()).data(pageData.getContent()).build();
        }
        return PaginationDTO.<List<UserDetailsProj>>builder().pageSize(0)
                .totalPages(0).totalElements(0L).data(Collections.emptyList()).build();

    }

    public UserDetailsProj getUserByUserName(String userName) {
        if (userName == null || StringUtils.isBlank(userName)) {
            return null;
        }

        UserDetailsProj userData = this.userRepo.findUserDetailsByUserName(userName);
        if (userData != null) {
            return userData;
        }
        return null;
    }

    public List<UserExperienceEntity> getUserExperienceByUserId(Integer userId) {
        if (userId == null) {
            return Collections.emptyList();
        }

        Optional<List<UserExperienceEntity>> userExpDataOptional = this.userExpRepo.findByUserIdOrderByFromDateDesc(userId);
        if (userExpDataOptional != null && userExpDataOptional.isPresent()) {
            return userExpDataOptional.get();
        }
        return Collections.emptyList();
    }
}
