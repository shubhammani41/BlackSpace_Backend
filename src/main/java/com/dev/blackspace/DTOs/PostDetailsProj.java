package com.dev.blackspace.DTOs;

import java.time.LocalDateTime;

public interface PostDetailsProj {

    Long getPostId();
    Long getUserId();
    String getUserName();
    String getProfilePictureUrl();
    String getPostCaption();
    String getVisibility();
    LocalDateTime getCreatedDate();

    String getPostContents();  // These are JSON strings
    String getPostHashtags();
    String getPostViews();
}

