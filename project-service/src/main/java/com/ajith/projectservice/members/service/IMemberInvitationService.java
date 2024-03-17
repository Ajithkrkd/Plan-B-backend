package com.ajith.projectservice.members.service;

import com.ajith.projectservice.entity.Project;
import com.ajith.projectservice.members.dto.InviteRequest;
import com.ajith.projectservice.utils.BasicResponse;
import org.springframework.http.ResponseEntity;

public interface IMemberInvitationService {
    ResponseEntity< BasicResponse > sentInviteToMember (InviteRequest inviteRequest, String authHeader, Project project);
}
