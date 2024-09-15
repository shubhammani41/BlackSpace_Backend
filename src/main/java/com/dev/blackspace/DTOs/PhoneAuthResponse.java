package com.dev.blackspace.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PhoneAuthResponse {
    private String userCountryCode;
    private String userPhoneNumber;
    private String userFirstName;
    private String userLastName;
}

