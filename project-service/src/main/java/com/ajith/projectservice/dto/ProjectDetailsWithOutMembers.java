package com.ajith.projectservice.dto;

import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDetailsWithOutMembers {
        private Long projectId;
        private String title;
        private String description;
        private String project_profile_url;
}
