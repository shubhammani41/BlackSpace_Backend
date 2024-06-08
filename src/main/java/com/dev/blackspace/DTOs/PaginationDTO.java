package com.dev.blackspace.DTOs;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaginationDTO<T> {
    private Integer totalPages;
    private Integer pageSize;
    private Long totalElements;
    private T data;

}
