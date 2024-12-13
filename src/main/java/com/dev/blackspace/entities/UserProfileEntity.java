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

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "profile_picture_url")
    private String profilePictureUrl;

    @Column(name = "bio")
    private String bio;

    @Column(name = "website_url")
    private String websiteUrl;

    @Column(name = "date_joined")
    private Date dateJoined;

    @Column(name = "experience")
    private Integer experience;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "country_id")
    private Integer countryId;

    @Column(name = "state_id")
    private Integer stateId;

    @Column(name = "city_id")
    private Integer cityId;

    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "position_id")
    private Integer positionId;

    @Column(name = "skill_ids")
    private String skillIds;

    @Column(name = "is_phone_private")
    private Boolean isPhonePrivate;

    @Column(name = "is_email_private")
    private Boolean isEmailPrivate;
}
