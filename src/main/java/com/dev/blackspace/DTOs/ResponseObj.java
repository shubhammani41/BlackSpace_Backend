package com.dev.blackspace.DTOs;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseObj {
    private Integer status;
    private String message;
    private List<ErrorObj> errorList;

    private Object data;
}
