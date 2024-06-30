package com.dev.blackspace.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Table(name = "user_experience")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserExperienceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "from_date")
    private LocalDate fromDate;

    @Column(name = "to_date")
    private LocalDate toDate;

    @Column(name = "organization_id", insertable=false, updatable=false)
    private Long organizationId;

    @ManyToOne
    @JoinColumn(name = "organization_id", referencedColumnName = "organization_id")
    private OrganizationEntity organizationDetails;

    @Column(name = "is_current_organization")
    private boolean currentOrganization;

    @Column(name = "description1")
    private String description1;

    @Column(name = "description2")
    private String description2;

    @Column(name = "description3")
    private String description3;

}
