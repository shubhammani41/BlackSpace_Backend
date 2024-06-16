package com.dev.blackspace.controllers;

import com.dev.blackspace.DTOs.ErrorObj;
import com.dev.blackspace.DTOs.PaginationDTO;
import com.dev.blackspace.DTOs.ResponseObj;
import com.dev.blackspace.DTOs.UserDetailsProj;
import com.dev.blackspace.entities.UserProfileEntity;
import com.dev.blackspace.repositories.UserProfileRepo;
import com.dev.blackspace.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserProfileRepo userProfileRepo;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @GetMapping("/public/getUserList")
    public List<UserProfileEntity> getUserList(){
        return this.userProfileRepo.findAll();
    }

    @GetMapping("/public/getRandomUserList")
    public ResponseEntity<ResponseObj> getRandomUserList(@RequestParam Integer pageNumber, @RequestParam Integer pageSize){
        ResponseObj response = null;

        try{
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            PaginationDTO<List<UserDetailsProj>> userList = this.userServiceImpl.getRandomUserListByPage(pageable);

            if(userList!=null && userList.getData()!=null){
                response = ResponseObj.builder().status(1).message("Data fetched successfully.").data(userList).build();
            }
            else{
                response = ResponseObj.builder().status(1).message("No data found!").data(userList).build();
            }
            return ResponseEntity.ok(response);
        }
        catch (Exception e){
            ErrorObj errorObj = ErrorObj.builder().errorCode("B_S_1").errorMessage("Exception occurred in fetching data!").build();
            List<ErrorObj> errorList = new ArrayList<>();
            errorList.add(errorObj);

            response = ResponseObj.builder().status(0).message("Oops! Something went wrong!").data(null).errorList(errorList).build();
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/public/searchUsersByKeyword")
    public ResponseEntity<ResponseObj> searchUsersByKeyword(@RequestParam Integer pageNumber, @RequestParam Integer pageSize, @RequestParam String searchKeyWord){
        ResponseObj response = null;

        try{
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            PaginationDTO<List<UserDetailsProj>> userList = this.userServiceImpl.searchUsersByKeyword(pageable, searchKeyWord);

            if(userList!=null && userList.getData()!=null){
                response = ResponseObj.builder().status(1).message("Data fetched successfully.").data(userList).build();
            }
            else{
                response = ResponseObj.builder().status(1).message("No data found!").data(userList).build();
            }
            return ResponseEntity.ok(response);
        }
        catch (Exception e){
            ErrorObj errorObj = ErrorObj.builder().errorCode("B_S_1").errorMessage("Exception occurred in fetching data!").build();
            List<ErrorObj> errorList = new ArrayList<>();
            errorList.add(errorObj);

            response = ResponseObj.builder().status(0).message("Oops! Something went wrong!").data(null).errorList(errorList).build();
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
