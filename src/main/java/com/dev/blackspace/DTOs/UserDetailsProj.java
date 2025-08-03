package com.dev.blackspace.DTOs;

import java.util.Date;

public interface UserDetailsProj {
    Long getUserId();

    Long getUserProfileId();

    String getFirstName();

    String getLastName();

    Date getDateOfBirth();

    String getProfilePictureUrl();

    String getGender();

    String getBio();

    String getWebsiteUrl();

    String getUserName();

    String getSkills(); // JSON string of skills

    String getUserExperience(); // JSON string of skills

    String getCountryName();

    String getStateName();

    String getCityName();

    String getPositionName();

    String getIndustryName();

    Integer getPositionId();

    Integer getIndustryId();

    Integer getRoleId();

    String getRoleName();

    Integer getExperience();

    String getEmail();

    String getPhoneNumber();

    Boolean getIsEmailPrivate();

    Boolean getIsPhonePrivate();

    Date getCreatedAt();

    Date getUpdatedAt();

    Long getUpdatedBy();

    Date getUserProfileCreatedAt();

    Date getUserProfileUpdatedAt();

    Long getUserProfileUpdatedBy();
}
