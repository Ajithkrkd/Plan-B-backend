package com.ajith.projectservice.dto.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectAdministratorDetails {
    private Long id;
    private String fullName;
    private String email;
    private String profile_image_url;
}
