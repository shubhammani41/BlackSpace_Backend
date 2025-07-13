package com.dev.blackspace.repositories;

import com.dev.blackspace.DTOs.PostDetailsProj;
import com.dev.blackspace.entities.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepo extends JpaRepository<PostEntity, Long> {
    String POST_DETAILS_QUERY = "SELECT \n" +
            "  p.post_id AS postId,\n" +
            "  p.user_id AS userId,\n" +
            "  up.user_name As userName,\n"+
            "  up.profile_picture_url As profilePictureUrl,\n"+
            "  p.post_caption AS postCaption,\n" +
            "  p.visibility,\n" +
            "  p.created_date AS createdDate,\n" +
            "\n" +
            "  -- Contents as JSON array\n" +
            "  (SELECT JSON_ARRAYAGG(\n" +
            "      JSON_OBJECT(\n" +
            "        'contentId', pc.content_id,\n" +
            "        'mediaLink', pc.media_link,\n" +
            "        'mediaType', pc.media_type,\n" +
            "        'mediaSize', pc.media_size,\n" +
            "        'mediaDuration', pc.media_duration,\n" +
            "        'thumbnailLink', pc.thumbnail_link,\n" +
            "        'createdDate', pc.created_date\n" +
            "      )\n" +
            "    )\n" +
            "   FROM post_content pc\n" +
            "   WHERE pc.post_id = p.post_id\n" +
            "  ) AS postContents,\n" +
            "\n" +
            "  -- Hashtags as JSON array\n" +
            "  (SELECT JSON_ARRAYAGG(\n" +
            "     JSON_OBJECT(\n" +
            "        'hashtagId', h.id,\n" +
            "        'hashtag', h.tag\n" +
            "    )\n" +
            "   )\n" +
            "   FROM post_hashtags ph\n" +
            "   JOIN hashtags h ON ph.hashtag_id = h.id\n" +
            "   WHERE ph.post_id = p.post_id\n" +
            "  ) AS postHashtags,\n"+
            "\n" +
            "  -- Views as JSON array\n" +
            "  (SELECT JSON_ARRAYAGG(\n" +
            "      JSON_OBJECT(\n" +
            "        'viewId', pv.view_id,\n" +
            "        'userId', pv.user_id,\n" +
            "        'reaction', pv.reaction,\n" +
            "        'timesViewed', pv.times_viewed,\n" +
            "        'viewedAt', pv.viewed_at,\n" +
            "        'userName', usp.user_name,\n"+
            "        'profilePictureUrl', usp.profile_picture_url\n"+
            "      )\n" +
            "    )\n" +
            "   FROM post_views pv\n" +
            "   JOIN user_profile usp ON usp.user_id = pv.user_id\n"+
            "   WHERE pv.post_id = p.post_id\n" +
            "  ) AS postViews\n" +
            "\n" +
            "FROM post p\n"+
            "JOIN user_profile up ON up.user_id = p.user_id\n";

    @Query(value = POST_DETAILS_QUERY +
            "WHERE p.user_id=:userId AND p.visibility='PUBLIC'\n" +
            "ORDER BY p.created_date\n",
            countQuery = "SELECT COUNT(*) FROM post p WHERE p.user_id=:userId AND p.visibility='PUBLIC'",
            nativeQuery = true)
    Page<PostDetailsProj> findPublicPostDetailsByUserId(Pageable pageable, Integer userId);

    @Query(value = POST_DETAILS_QUERY +
            "WHERE p.visibility='PUBLIC'\n" +
            "ORDER BY p.created_date\n",
            countQuery = "SELECT COUNT(*) FROM post p WHERE p.visibility='PUBLIC'",
            nativeQuery = true)
    Page<PostDetailsProj> findPublicFeed(Pageable pageable);
}
