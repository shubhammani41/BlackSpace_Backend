package com.dev.blackspace.DTOs;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginReqDTO {
    private String userJsonUrl;

    //0 for phone 1 for email
    private Integer authType;
}
