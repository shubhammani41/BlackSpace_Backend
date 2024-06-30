package com.dev.blackspace.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "organizations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "organization_id")
    private Long organizationId;

    @Column(name = "organization_name", unique = true, nullable = false)
    private String organizationName;

    @Column(name = "address")
    private String address;

    @Column(name = "website")
    private String website;

    @Column(name = "industry")
    private String industry;

    @Column(name = "founded_year")
    private Integer foundedYear;
}
