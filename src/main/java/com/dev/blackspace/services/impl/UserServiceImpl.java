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
    private UserProfileRepo userProfileRepo;

    @Autowired
    private UserExperienceRepo userExpRepo;

    @Autowired
    private StringUtil stringUtil;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserDetailsDTOMapper userDetailsDTOMapper;

    public UserServiceImpl() {
    }

    public PaginationDTO<List<UserDetailsDTO>> getRandomUserListByPage(Pageable pageable) {
        Page<UserDetailsProj> pageData = this.userProfileRepo.findUserDetailsByRandomAndPage(pageable);
        if (pageData != null) {
            return PaginationDTO.<List<UserDetailsDTO>>builder().pageSize(pageData.getSize())
                    .totalPages(pageData.getTotalPages()).totalElements(pageData.getTotalElements()).data(userDetailsDTOMapper.toUserDetailsDTOList(pageData.getContent())).build();
        }
        return PaginationDTO.<List<UserDetailsDTO>>builder().pageSize(0)
                .totalPages(0).totalElements(0L).data(Collections.emptyList()).build();
    }

    public PaginationDTO<List<UserDetailsDTO>> searchUsersByKeyword(Pageable pageable, String searchKeyWord) {
        String searchRegex = this.stringUtil.getSearchRegex(searchKeyWord);
        Page<UserDetailsProj> pageData = this.userProfileRepo.findUserDetailsBySearchKeyWord(pageable, searchRegex);
        if (pageData != null) {
            return PaginationDTO.<List<UserDetailsDTO>>builder().pageSize(pageData.getSize())
                    .totalPages(pageData.getTotalPages()).totalElements(pageData.getTotalElements()).data(userDetailsDTOMapper.toUserDetailsDTOList(pageData.getContent())).build();
        }
        return PaginationDTO.<List<UserDetailsDTO>>builder().pageSize(0)
                .totalPages(0).totalElements(0L).data(Collections.emptyList()).build();
    }

    public UserDetailsDTO getUserByUserName(String userName) {
        if (userName == null || StringUtils.isBlank(userName)) {
            return null;
        }

        UserDetailsProj userData = this.userProfileRepo.findUserDetailsByUserName(userName);
        if (userData != null) {
            return userDetailsDTOMapper.toUserDetailsDTO(userData);
        }
        return null;
    }

    public UserProfileEntity getUserByUserLoginId(String userLoginId) {
        if (userLoginId == null || StringUtils.isBlank(userLoginId)) {
            return null;
        }
        UserLoginEntity userLoginData = this.userLoginRepo.findByUserId(Long.valueOf(userLoginId));
        if(Objects.isNull(userLoginData) || Objects.isNull(userLoginData.getUserProfileId())){
            return null;
        }
        UserProfileEntity userProfileDetails = this.userProfileRepo.findByUserId(userLoginData.getUserProfileId());
        if(Objects.isNull(userProfileDetails)){
            return null;
        }
        return userProfileDetails;
    }

    public List<UserExperienceEntity> getUserExperienceByUserId(Integer userId) {
        if (userId == null) {
            return Collections.emptyList();
        }

        List<UserExperienceEntity> userExpDataOptional = this.userExpRepo.findByUserIdOrderByFromDateDesc(userId);
        if (userExpDataOptional != null) {
            return userExpDataOptional;
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
                        UserLoginEntity userLoginEntity = this.findUserLoginByEmailAndInsert(userRecord.getEmail());
                        if(!userLoginEntity.getIsDeactivated()){
                            String token = this.jwtUtil.generateToken(userRecord.getEmail());
                            userLoginResDTO.setToken(token);
                            userLoginResDTO.setUserDetails(userLoginEntity);
                        }

                    }
                    if(Objects.nonNull(userRecord.getPhoneNumber()) && !userRecord.getPhoneNumber().isBlank()){
                        UserLoginEntity userLoginEntity = this.findUserLoginByPhoneAndInsert(userRecord.getPhoneNumber());
                        if(!userLoginEntity.getIsDeactivated()){
                            String token = this.jwtUtil.generateToken(userRecord.getEmail());
                            userLoginResDTO.setToken(token);
                            userLoginResDTO.setUserDetails(userLoginEntity);
                        }
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

    public UserLoginEntity findUserLoginByPhoneAndInsert(String phoneNumber){
        UserLoginEntity userLoginEntity = this.userLoginRepo.findByPhoneOrEmail(phoneNumber);
        if(Objects.isNull(userLoginEntity)){
            userLoginEntity = new UserLoginEntity();
            userLoginEntity.setPhoneNumber(phoneNumber);
            userLoginEntity.setCreatedAt(new Date());
            userLoginRepo.save(userLoginEntity);
        }

        return userLoginEntity;
    }

    public UserLoginEntity findUserLoginByEmailAndInsert(String email){
        UserLoginEntity userLoginEntity = this.userLoginRepo.findByPhoneOrEmail(email);
        if(Objects.isNull(userLoginEntity)){
            userLoginEntity = new UserLoginEntity();
            userLoginEntity.setEmail(email);
            userLoginEntity.setCreatedAt(new Date());
            userLoginRepo.save(userLoginEntity);
        }
        return userLoginEntity;
    }

    public UserProfileEntity createBasicDetailsByUserLoginId(String userLoginId, UserProfileEntity userProfileData){
        if (userLoginId == null || StringUtils.isBlank(userLoginId)) {
            return null;
        }
        UserLoginEntity userLoginData = this.userLoginRepo.findByUserId(Long.valueOf(userLoginId));
        if(Objects.isNull(userLoginData) || Objects.nonNull(userLoginData.getUserProfileId())){
            return null;
        }
        userProfileRepo.save(userProfileData);
        userLoginData.setUserProfileId(userProfileData.getUserId());
        userLoginRepo.save(userLoginData);
        return userProfileData;
    }
}
