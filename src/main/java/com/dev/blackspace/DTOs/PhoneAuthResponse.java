package com.dev.blackspace.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PhoneAuthResponse {
    @JsonProperty("user_country_code")
    private String userCountryCode;

    @JsonProperty("user_phone_number")
    private String userPhoneNumber;

    @JsonProperty("user_first_name")
    private String userFirstName;

    @JsonProperty("user_last_name")
    private String userLastName;
}

