package com.dev.blackspace.services.impl;

import com.dev.blackspace.DTOs.PaginationDTO;
import com.dev.blackspace.DTOs.UserDetailsProj;
import com.dev.blackspace.entities.UserProfileEntity;
import com.dev.blackspace.repositories.UserProfileRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl {

    @Autowired
    private UserProfileRepo userRepo;

    public PaginationDTO<List<UserDetailsProj>> getRandomUserListByPage(Pageable pageable) {
        Page<UserDetailsProj> pageData = this.userRepo.findUserDetailsByRandomAndPage(pageable);
        if (pageData != null) {
            return PaginationDTO.<List<UserDetailsProj>>builder().pageSize(pageData.getSize())
                    .totalPages(pageData.getTotalPages()).totalElements(pageData.getTotalElements()).data(pageData.getContent()).build();
        }

        return PaginationDTO.<List<UserDetailsProj>>builder().pageSize(0)
                .totalPages(0).totalElements(0L).data(Collections.emptyList()).build();

    }
}
