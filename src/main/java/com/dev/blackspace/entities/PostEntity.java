package com.dev.blackspace.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "post")
public class PostEntity {

    @Id
    @Column(name = "post_id")
    private Long postId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "post_caption")
    private String postCaption;

    @Column(name = "visibility", nullable = false)
    private String visibility;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;
}
