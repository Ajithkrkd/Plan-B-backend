package com.ajith.workItemservice.comments.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
public class Comment {
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long commentId;
    private String content;
    private LocalDateTime commentedOn;
    private Long commentedBy;
    private boolean isEdited = false;
    private boolean isDeleted = false;


}
