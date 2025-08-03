package com.dev.blackspace.DTOs;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDetailsDTO {
    private Long userId;
    private Long userProfileId;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String profilePictureUrl;
    private String gender;
    private String bio;
    private String websiteUrl;
    private String userName;
    private Object skills; // Parsed JSON
    private Object userExperience; // Parsed JSON
    private String countryName;
    private String stateName;
    private String cityName;
    private String positionName;
    private String industryName;
    private Integer positionId;
    private Integer industryId;
    private Integer roleId;
    private String roleName;
    private Integer experience;
    private String email;
    private String phoneNumber;
    private Boolean isEmailPrivate;
    private Boolean isPhonePrivate;
}

