package com.ajith.projectservice.members.service;

import com.ajith.projectservice.entity.Project;
import com.ajith.projectservice.members.dto.InviteRequest;
import com.ajith.projectservice.members.dto.MemberInvitationResponse;
import com.ajith.projectservice.members.entity.MemberInvitation;
import com.ajith.projectservice.utils.BasicResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IMemberInvitationService {
    ResponseEntity< BasicResponse > sentInviteToMember (InviteRequest inviteRequest, String authHeader, Project project);

    ResponseEntity< BasicResponse> acceptTheInviteRequest (String token, String authHeader);

    ResponseEntity< List < MemberInvitationResponse > > getAllInvitations (String authHeader);
}
