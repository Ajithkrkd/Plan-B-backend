package com.ajith.projectservice.dto.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignedMembersDetails {
    private Long id;
    private String fullName;
    private String email;
}
