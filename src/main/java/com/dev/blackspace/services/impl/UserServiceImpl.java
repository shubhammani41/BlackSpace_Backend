package com.dev.blackspace.services.impl;

import com.dev.blackspace.DTOs.*;
import com.dev.blackspace.entities.UserExperienceEntity;
import com.dev.blackspace.entities.UserLoginEntity;
import com.dev.blackspace.entities.UserProfileEntity;
import com.dev.blackspace.repositories.UserExperienceRepo;
import com.dev.blackspace.repositories.UserLoginRepo;
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

import java.util.*;

@Service
@Slf4j
public class UserServiceImpl {

    @Autowired
    private UserLoginRepo userLoginRepo;

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
                    if(phoneAuthResponse!=null && phoneAuthResponse.getUserPhoneNumber()!=null && !phoneAuthResponse.getUserPhoneNumber().isBlank()){
                        UserLoginResDetailsDTO userLoginResDetailsDTO = this.findUserLoginByPhoneAndInsert(phoneAuthResponse.getUserPhoneNumber(), phoneAuthResponse.getUserCountryCode());
                        userLoginResDTO.setToken(jwtUtil.generateToken(phoneAuthResponse.getUserPhoneNumber()));
                        userLoginResDTO.setUserDetails(userLoginResDetailsDTO);
                    }
                }
                catch (Exception e){
                    log.error("Inside catch block of getUserLoginDetailsFromUserJsonUrl():::Error generating phone token:::{}",e);
                }

            } else if (authType.equals(1)) {
                try{
                    EmailAuthResponse emailAuthResponse = objectMapper.readValue(userOTPData, EmailAuthResponse.class);
                    if(emailAuthResponse!=null && emailAuthResponse.getUserEmailId()!=null && !emailAuthResponse.getUserEmailId().isBlank()){
                        UserLoginResDetailsDTO userLoginResDetailsDTO = this.findUserLoginByEmailAndInsert(emailAuthResponse.getUserEmailId());
                        userLoginResDTO.setToken(jwtUtil.generateToken(emailAuthResponse.getUserEmailId()));
                        userLoginResDTO.setUserDetails(userLoginResDetailsDTO);
                    }
                }
                catch (Exception e){
                    log.error("Inside catch block of getUserLoginDetailsFromUserJsonUrl():::Error generating email token:::{}",e);
                }
            }
        }
        return userLoginResDTO;
    }

    public UserLoginResDetailsDTO findUserLoginByPhoneAndInsert(String phoneNumber, String countryCode){
        UserLoginEntity userLoginEntity = this.userLoginRepo.findByPhoneOrEmail(phoneNumber);
        if(Objects.isNull(userLoginEntity)){
            userLoginEntity = new UserLoginEntity();
            userLoginEntity.setPhoneNumber(phoneNumber);
            userLoginEntity.setPhoneCountryCode(countryCode);
            userLoginEntity.setCreatedAt(new Date());
            userLoginRepo.save(userLoginEntity);
        }
        UserLoginResDetailsDTO userLoginResDetailsDTO = UserLoginResDetailsDTO.builder().userId(userLoginEntity.getUserId()).userEmail(userLoginEntity.getEmail())
                .userPhoneNumber(userLoginEntity.getPhoneNumber()).userProfileId(userLoginEntity.getUserProfileId())
                .phoneCountryCode(userLoginEntity.getPhoneCountryCode()).build();
        return userLoginResDetailsDTO;
    }

    public UserLoginResDetailsDTO findUserLoginByEmailAndInsert(String email){
        UserLoginEntity userLoginEntity = this.userLoginRepo.findByPhoneOrEmail(email);
        if(Objects.isNull(userLoginEntity)){
            userLoginEntity = new UserLoginEntity();
            userLoginEntity.setEmail(email);
            userLoginEntity.setCreatedAt(new Date());
            userLoginRepo.save(userLoginEntity);
        }
        UserLoginResDetailsDTO userLoginResDetailsDTO = UserLoginResDetailsDTO.builder().userId(userLoginEntity.getUserId()).userEmail(userLoginEntity.getEmail())
                .userPhoneNumber(userLoginEntity.getPhoneNumber()).userProfileId(userLoginEntity.getUserProfileId())
                .phoneCountryCode(userLoginEntity.getPhoneCountryCode()).build();

        return userLoginResDetailsDTO;
    }
}
