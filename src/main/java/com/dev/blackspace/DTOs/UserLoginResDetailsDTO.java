package com.dev.blackspace.DTOs;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginResDetailsDTO {
    private Long userId;
    private String userPhoneNumber;
    private String userEmail;
    private Long userProfileId;
    private String phoneCountryCode;
}
