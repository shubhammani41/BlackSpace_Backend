package com.dev.blackspace.DTOs;

import java.util.Date;

public interface UserDetailsProj {
    Long getUserId();

    String getFirstName();

    String getLastName();

    String getEmail();

    Date getDateOfBirth();

    String getProfilePictureUrl();

    String getGender();

    String getBio();

    String getGithubUrl();

    String getLinkedinUrl();

    String getTwitterUrl();

    String getWebsiteUrl();

    String getUserName();

    String getSkills(); // JSON string of skills

    String getCountryName();

    String getStateName();

    String getCityName();

    String getPositionName();

    String getIndustryName();

    String getOrganizationName();

    Integer getCountryId();

    Integer getStateId();

    Integer getCityId();

    Integer getPositionId();

    Integer getIndustryId();

    Integer getOrganizationId();

    Integer getRoleId();

    String getRoleName();

    Integer getExperience();

    String getPhoneNumber();

    String getCallingCode();
}
