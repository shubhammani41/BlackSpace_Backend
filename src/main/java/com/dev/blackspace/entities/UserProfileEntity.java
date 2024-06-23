package com.dev.blackspace.entities;

import com.dev.blackspace.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "user_profile")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "email", nullable = false, length = 100, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @Column(name = "calling_code", length = 5)
    private String callingCode;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 10)
    private Gender gender;

    @Column(name = "profile_picture_url", length = 255)
    private String profilePictureUrl;

    @Column(name = "bio", length = 500)
    private String bio;

    @Column(name = "github_url", length = 255)
    private String githubUrl;

    @Column(name = "linkedin_url", length = 255)
    private String linkedinUrl;

    @Column(name = "facebook_url", length = 255)
    private String facebookUrl;

    @Column(name = "twitter_url", length = 255)
    private String twitterUrl;

    @Column(name = "website_url", length = 255)
    private String websiteUrl;

    @Column(name = "date_joined")
    private Date dateJoined;

    @Column(name = "experience")
    private Integer experience;

    @Column(name = "user_name", nullable = false, length = 30, unique = true)
    private String userName;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "country_id")
    private Integer countryId;

    @Column(name = "state_id")
    private Integer stateId;

    @Column(name = "city_id")
    private Integer cityId;

    @Column(name = "is_deactivated", nullable = false)
    private Boolean isDeactivated;

    @Column(name = "deactivated_at")
    private Date deactivatedAt;

    @Column(name = "role_id", nullable = false)
    private Integer roleId;

    @Column(name = "position_id", nullable = false)
    private Integer positionId;

    @Column(name = "organization_id", nullable = false)
    private Integer organizationId;
}
