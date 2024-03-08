package com.ajith.projectservice.dto.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectAdministratorDetails {
    private Long administrator_id;
    private String fullName;
    private String email;
}
