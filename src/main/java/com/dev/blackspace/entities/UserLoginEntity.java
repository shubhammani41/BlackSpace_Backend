package com.dev.blackspace.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user_login")
public class UserLoginEntity {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "user_profile_id", unique = true)
    private Long userProfileId;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "phone_country_code")
    private String phoneCountryCode;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "is_deactive")
    private Boolean isDeactive = false;

    @Column(name = "deactivated_at")
    private Date deactivatedAt;
}
