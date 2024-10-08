package com.dev.blackspace.services.impl;

import com.dev.blackspace.DTOs.*;
import com.dev.blackspace.entities.UserExperienceEntity;
import com.dev.blackspace.repositories.UserExperienceRepo;
import com.dev.blackspace.repositories.UserProfileRepo;
import com.dev.blackspace.utils.JWTUtil;
import com.dev.blackspace.utils.StringUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl {

    @Autowired
    private UserProfileRepo userRepo;

    @Autowired
    private UserExperienceRepo userExpRepo;

    @Autowired
    private StringUtil stringUtil;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    JWTUtil jwtUtil;

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

    public UserLoginResDTO getUserLoginDetailsFromUserJsonUrl(String userJsonUrl, Integer authType){
        UserLoginResDTO userLoginResDTO = new UserLoginResDTO();
        if(stringUtil.verifyDomain(userJsonUrl)){
            String userOTPData = this.restTemplate.getForObject(userJsonUrl, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

            if(authType.equals(0)){
                try{
                    PhoneAuthResponse phoneAuthResponse = objectMapper.readValue(userOTPData, PhoneAuthResponse.class);
                    userLoginResDTO.setToken(jwtUtil.generateToken(phoneAuthResponse.getUserPhoneNumber()));
                }
                catch (Exception e){
                    userLoginResDTO.setToken(null);
                    log.error("Inside catch block of getUserLoginDetailsFromUserJsonUrl():::Error generating phone token:::{}",e);
                }

            } else if (authType.equals(1)) {
                try{
                    EmailAuthResponse emailAuthResponse = objectMapper.readValue(userOTPData, EmailAuthResponse.class);
                    userLoginResDTO.setToken(jwtUtil.generateToken(emailAuthResponse.getUserEmailId()));
                }
                catch (Exception e){
                    userLoginResDTO.setToken(null);
                    log.error("Inside catch block of getUserLoginDetailsFromUserJsonUrl():::Error generating email token:::{}",e);
                }
            }
            else{
                userLoginResDTO.setToken(null);
            }
        }
        else{
            userLoginResDTO.setToken(null);
        }
        return userLoginResDTO;
    }
}
