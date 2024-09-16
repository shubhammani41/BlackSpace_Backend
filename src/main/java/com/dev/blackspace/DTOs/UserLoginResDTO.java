package com.dev.blackspace.DTOs;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginResDTO {
    private String token;
    private UserLoginResDetailsDTO userDetails;
}
