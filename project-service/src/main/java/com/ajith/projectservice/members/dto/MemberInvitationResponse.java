package com.ajith.projectservice.members.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MemberInvitationResponse {
    private Long invitation_id;
    private String project_image_url;
    private String project_title;
    private String invitation_from;
    private Date invitation_sent_time;
    private Date invitation_expires_time;
    private String invitation_status;
    private String invitation_message;
    private String invitation_token;
}
