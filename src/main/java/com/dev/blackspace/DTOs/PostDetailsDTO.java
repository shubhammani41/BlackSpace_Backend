package com.dev.blackspace.DTOs;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDetailsDTO {
    private Long postId;
    private Long userId;
    private String userName;
    private String profilePictureUrl;
    private String postCaption;
    private String visibility;
    private LocalDateTime createdDate;

    private Object postContents;
    private Object postHashtags;
    private Object postViews;
}
