package com.dev.blackspace.repositories;

import com.dev.blackspace.DTOs.UserDetailsProj;
import com.dev.blackspace.entities.UserProfileEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserProfileRepo extends JpaRepository<UserProfileEntity, Long> {

    String USER_DETAIL_QUERY = "SELECT ul.user_id as userId, ul.user_profile_id as userProfileId,\n" +

//            need to provide privacy settings before displaying phone and email
//            "ul.email as email, ul.phone_number as phoneNumber,\n" +

            "up.first_name as firstName, up.last_name as lastName, up.experience as experience, up.date_of_birth as dateOfBirth, up.profile_picture_url as profilePictureUrl, up.gender as gender, up.bio as bio, up.website_url as websiteUrl, up.user_name as userName, sd.skills, ed.user_experience,\n" +
            "cnt.country_name as countryName, cnt.country_id as countryId,\n" +
            "sts.state_name as StateName, sts.state_id as stateId,\n" +
            "cts.city_name as cityName, cts.city_id as cityId, post.position_name as positionName, post.position_id as positionId,\n" +
            "inds.industry_name as industryName, inds.industry_id as IndustryId,\n" +
            "role.role_name as roleName, role.role_id as roleId\n" +
            "FROM user_login ul\n" +
            "LEFT JOIN user_profile as up on ul.user_profile_id = up.user_id\n" +
            "LEFT JOIN countries cnt on cnt.country_id = up.country_id\n" +
            "LEFT JOIN states sts on sts.state_id = up.state_id\n" +
            "LEFT JOIN cities cts on cts.city_id = up.city_id\n" +
            "LEFT JOIN positions post on post.position_id = up.position_id\n" +
            "LEFT JOIN industries inds on inds.industry_id = post.industry_id\n" +
            "LEFT JOIN role role on role.role_id = up.role_id\n" +

            "LEFT JOIN (SELECT up.user_id,json_arrayagg(json_object(\"skill_id\",sk.skill_id, \"skill_name\",sk.skill_name)) as skills\n" +
            "FROM user_profile up\n" +
            "LEFT JOIN skills sk ON FIND_IN_SET(sk.skill_id, up.skill_ids)\n" +
            "GROUP BY up.user_id) AS sd ON sd.user_id = up.user_id\n" +

            "LEFT JOIN (SELECT up.user_id,\n" +
            "IF(COUNT(exp.user_id > 0),json_arrayagg(json_object(\"organization_id\",exp.organization_id, \"organization_name\",exp.organization_name, \"from_date\",exp.from_date)),'[]') as user_experience\n" +
            "FROM user_profile up\n" +
            "LEFT JOIN user_experience exp ON exp.user_id = up.user_id\n" +
            "GROUP BY up.user_id) AS ed ON ed.user_id = up.user_id\n"+

            "WHERE up.is_deactivated = 0\n";

    UserProfileEntity findByUserName(String userName);

    UserProfileEntity findByUserId(Long userId);

    @Query(value = USER_DETAIL_QUERY +
            "GROUP BY up.user_id\n", nativeQuery = true)
    Page<UserDetailsProj> findUserDetailsByRandomAndPage(Pageable pageable);

    @Query(value = USER_DETAIL_QUERY +
            "AND (\n" +
            "    LOWER(up.first_name) REGEXP :searchRegex OR\n" +
            "    LOWER(up.last_name) REGEXP :searchRegex OR\n" +
            "    LOWER(up.user_name) REGEXP :searchRegex OR\n" +
            "    LOWER(post.position_name) REGEXP :searchRegex OR\n" +
            "    LOWER(cnt.country_name) REGEXP :searchRegex OR\n" +
            "    LOWER(cts.city_name) REGEXP :searchRegex OR\n" +
            "    LOWER(sts.state_name) REGEXP :searchRegex OR\n" +
            "    EXISTS (\n" +
            "        SELECT 1 FROM skills sk_inner\n" +
            "        WHERE FIND_IN_SET(sk_inner.skill_id, up.skill_ids)\n" +
            "        AND LOWER(sk_inner.skill_name) REGEXP :searchRegex\n" +
            "    ) OR\n" +
            "    EXISTS (\n" +
            "       SELECT 1 FROM user_experience exp\n" +
            "       WHERE exp.user_id = ul.user_id\n" +
            "       AND LOWER(exp.organization_name) REGEXP :searchRegex\n" +
            "   )\n"+
            ")\n" +
            "GROUP BY up.user_id\n",
            nativeQuery = true)
    Page<UserDetailsProj> findUserDetailsBySearchKeyWord(Pageable pageable, String searchRegex);

    @Query(value = USER_DETAIL_QUERY +
            "AND up.user_name = :userName\n" +
            "GROUP BY up.user_id\n",
            nativeQuery = true)
    UserDetailsProj findUserDetailsByUserName(String userName);




}
