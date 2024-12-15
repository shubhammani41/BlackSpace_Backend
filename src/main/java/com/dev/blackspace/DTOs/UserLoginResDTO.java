package com.dev.blackspace.DTOs;

import com.dev.blackspace.entities.UserLoginEntity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginResDTO {
    private String token;
    private UserLoginEntity userDetails;
}
