package com.ajith.projectservice.members.mappers;

import com.ajith.projectservice.members.dto.MemberInvitationResponse;
import com.ajith.projectservice.members.entity.MemberInvitation;
import com.ajith.projectservice.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectDtoMapper {

private final ProjectRepository projectRepository;


    public MemberInvitationResponse mapInvitationToInvitationResponse (MemberInvitation invitation) {
        return MemberInvitationResponse.builder()
                .invitation_id ( invitation.getId () )
                .invitation_from ( invitation.getInviteFrom () )
                .project_title ( invitation.getProject ().getTitle () )
                .project_image_url ( invitation.getProject ().getProject_profile_url () )
                .invitation_message ( invitation.getInvitationMessage () )
                .invitation_status ( String.valueOf ( invitation.getStatus () ) )
                .invitation_expires_time ( invitation.getExpiryDate () )
                .invitation_sent_time ( invitation.getCreatedAt () )
                .invitation_token ( invitation.getInvitationToken () )
                .build();
    }
}
