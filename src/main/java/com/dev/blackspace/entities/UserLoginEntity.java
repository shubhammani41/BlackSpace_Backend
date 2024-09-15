package com.dev.blackspace.entities;

import jakarta.persistence.*;
import lombok.*;

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
}
