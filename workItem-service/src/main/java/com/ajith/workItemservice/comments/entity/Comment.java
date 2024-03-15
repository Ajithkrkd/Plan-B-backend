package com.ajith.workItemservice.comments.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long commentId;
    private String content;
    private LocalDateTime commentedOn;
    private LocalDateTime commentedBy;
    private boolean isDeleted;
}
