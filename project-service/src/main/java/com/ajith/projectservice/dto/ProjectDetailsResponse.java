package com.ajith.projectservice.dto;

import com.ajith.projectservice.dto.user.AssignedMembersDetails;
import com.ajith.projectservice.dto.user.ProjectAdministratorDetails;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDetailsResponse {
    private Long projectId;
    private String title;
    private String description;
    private String project_profile_url;
    private List < AssignedMembersDetails > assignedMembersDetailsList;
    private List < ProjectAdministratorDetails > projectAdministratorDetailsList;
    private LocalDateTime createdDateTime;
}
