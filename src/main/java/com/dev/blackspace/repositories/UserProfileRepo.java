package com.dev.blackspace.repositories;

import com.dev.blackspace.DTOs.UserDetailsProj;
import com.dev.blackspace.entities.UserProfileEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserProfileRepo extends JpaRepository<UserProfileEntity, Long> {

    String USER_DETAIL_QUERY = "select up.user_id as userId, up.first_name as firstName, up.last_name as lastName, up.email as email, up.experience as experience, up.date_of_birth as dateOfBirth, up.profile_picture_url as profilePictureUrl,\n" +
            "up.gender as gender, up.bio as bio, up.github_url as githubUrl, up.linkedin_url as linkedUrl, up.twitter_url as twitterUrl, up.website_url as websiteUrl,\n" +
            "up.user_name as userName, json_arrayagg(json_object(\"skill_id\",sk.skill_id, \"skill_name\",sk.skill_name)) as skills, \n" +
            "cnt.country_name as countryName, cnt.country_id as countryId, sts.state_name as StateName, sts.state_id as stateId,\n" +
            "cts.city_name as cityName, cts.city_id as cityId, post.position_name as positionName, post.position_id as positionId,\n" +
            "inds.industry_name as industryName, inds.industry_id as IndustryId, \n" +
            "orgs.organization_name as organizationName, orgs.organization_id as organizationId,\n" +
            "role.role_name as roleName, role.role_id as roleId\n" +
            "from user_profile up \n" +
            "join skills sk on FIND_IN_SET(sk.skill_id, up.skill_ids)\n" +
            "join countries cnt on cnt.country_id = up.country_id\n" +
            "join states sts on sts.state_id = up.state_id\n" +
            "join cities cts on cts.city_id = up.city_id\n" +
            "join positions post on post.position_id = up.position_id\n" +
            "join industries inds on inds.industry_id = post.industry_id\n" +
            "join organizations orgs on orgs.organization_id = up.organization_id\n" +
            "join role role on role.role_id = up.role_id\n" +
            "where up.is_active = 1\n";

    UserProfileEntity findByUserName(String userName);

    @Query(value = USER_DETAIL_QUERY +
            "group by up.user_id ", nativeQuery = true)
    Page<UserDetailsProj> findUserDetailsByRandomAndPage(Pageable pageable);

    @Query(value = USER_DETAIL_QUERY +
            "AND (" +
            "LOWER(up.first_name) REGEXP :searchRegex " +
            "OR LOWER(up.last_name) REGEXP :searchRegex " +
            "OR LOWER(post.position_name) REGEXP :searchRegex " +
            "OR LOWER(sk.skill_name) REGEXP :searchRegex " +
            "OR LOWER(cnt.country_name) REGEXP :searchRegex " +
            "OR LOWER(cts.city_name) REGEXP :searchRegex " +
            "OR LOWER(sts.state_name) REGEXP :searchRegex " +
            ") " +
            "GROUP BY up.user_id ",
            nativeQuery = true)
    Page<UserDetailsProj> findUserDetailsBySearchKeyWord(Pageable pageable, String searchRegex);



}
