package com.dev.blackspace.DTOs;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorObj {
    private String errorCode;
    private String errorMessage;
}
