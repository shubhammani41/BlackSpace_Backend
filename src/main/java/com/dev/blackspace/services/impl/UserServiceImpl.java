package com.dev.blackspace.services.impl;

import com.dev.blackspace.DTOs.*;
import com.dev.blackspace.entities.UserExperienceEntity;
import com.dev.blackspace.entities.UserLoginEntity;
import com.dev.blackspace.repositories.UserExperienceRepo;
import com.dev.blackspace.repositories.UserLoginRepo;
import com.dev.blackspace.repositories.UserProfileRepo;
import com.dev.blackspace.utils.JWTUtil;
import com.dev.blackspace.utils.StringUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
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

    public UserLoginResDTO verifyFirebaseToken(String firebaseToken){
        UserLoginResDTO userLoginResDTO = new UserLoginResDTO();
        try{
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(firebaseToken);
            String uid = decodedToken.getUid();
            if(Objects.nonNull(uid) && !uid.isBlank()){
                UserRecord userRecord = FirebaseAuth.getInstance().getUser(uid);
                if(Objects.nonNull(userRecord)){
                    if(Objects.nonNull(userRecord.getEmail()) && !userRecord.getEmail().isBlank()){
                        UserLoginResDetailsDTO userDetails = this.findUserLoginByEmailAndInsert(userRecord.getEmail());
                        String token = this.jwtUtil.generateToken(userRecord.getEmail());
                        userLoginResDTO.setToken(token);
                        userLoginResDTO.setUserDetails(userDetails);

                    }
                    if(Objects.nonNull(userRecord.getPhoneNumber()) && !userRecord.getPhoneNumber().isBlank()){
                        UserLoginResDetailsDTO userDetails = this.findUserLoginByPhoneAndInsert(userRecord.getPhoneNumber());
                        String token = this.jwtUtil.generateToken(userRecord.getEmail());
                        userLoginResDTO.setToken(token);
                        userLoginResDTO.setUserDetails(userDetails);
                    }
                }
            }
        }
        catch(Exception e){
            log.error(":::Exception during firebase token verification:::",e);
            return new UserLoginResDTO();
        }
        return userLoginResDTO;
    }

    public UserLoginResDetailsDTO findUserLoginByPhoneAndInsert(String phoneNumber){
        UserLoginEntity userLoginEntity = this.userLoginRepo.findByPhoneOrEmail(phoneNumber);
        if(Objects.isNull(userLoginEntity)){
            userLoginEntity = new UserLoginEntity();
            userLoginEntity.setPhoneNumber(phoneNumber);
            userLoginEntity.setCreatedAt(new Date());
            userLoginRepo.save(userLoginEntity);
        }
        UserLoginResDetailsDTO userLoginResDetailsDTO = UserLoginResDetailsDTO.builder().userId(userLoginEntity.getUserId()).userEmail(userLoginEntity.getEmail())
                .userPhoneNumber(userLoginEntity.getPhoneNumber()).userProfileId(userLoginEntity.getUserProfileId()).build();
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
                .userPhoneNumber(userLoginEntity.getPhoneNumber()).userProfileId(userLoginEntity.getUserProfileId()).build();

        return userLoginResDetailsDTO;
    }
}
