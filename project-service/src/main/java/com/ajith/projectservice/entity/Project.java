package com.ajith.projectservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO )
    private Long id;
    private String title;
    private String description;
    private String project_profile_url;
    private LocalDateTime createdAt;
    private boolean is_deleted;
    private String projectRootAdministrator;

    @ElementCollection
    private List<Long> projectAdministrators;
    @ElementCollection
    private List <Long> assignedMembersIds;
}
