package com.ajith.projectservice.members.controller;

import com.ajith.jwtutilpackage.jwt.JwtService;
import com.ajith.projectservice.entity.Project;
import com.ajith.projectservice.exceptions.ResourceNotFoundException;
import com.ajith.projectservice.members.dto.InviteRequest;
import com.ajith.projectservice.members.service.IMemberInvitationService;
import com.ajith.projectservice.repository.ProjectRepository;
import com.ajith.projectservice.service.IProjectService;
import com.ajith.projectservice.utils.BasicResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/project/members")
@RequiredArgsConstructor
public class MembersInvitationController {

    private final IMemberInvitationService iMemberInvitationService;
    private final ProjectRepository projectRepository;
    @PostMapping("/invite/{projectId}")
    public ResponseEntity< BasicResponse > sentInvitationForJoiningToProject(
            @PathVariable("projectId") String projectId,
            @RequestHeader("Authorization") String authHeader,
            @RequestBody InviteRequest inviteRequest) {

        try {
            if (inviteRequest.getEmail() == null || inviteRequest.getMessage() == null) {
                System.out.println (inviteRequest.getEmail () );
                System.out.println (inviteRequest.getMessage () );
                return ResponseEntity.badRequest()
                        .body( BasicResponse.builder ()
                                .status(HttpStatus.BAD_REQUEST.value ( ) )
                                .message ( "Your invite request missing something" )
                                .description ( inviteRequest.getEmail () + inviteRequest.getMessage () )
                                .timestamp ( LocalDateTime.now () )
                                .build ());
            }
            Long id = Long.valueOf ( projectId );
            Optional < Project > optionalProject = projectRepository.findById ( id );
            if(!optionalProject.isPresent ()){
                throw new ResourceNotFoundException ( "project does not exist with given project id" );
            }
            //TODO: only admins can invite members
                Project existingProject = optionalProject.get ();
                iMemberInvitationService.sentInviteToMember(inviteRequest,authHeader,existingProject);

        }catch (ResourceNotFoundException e){
            throw e;
        }

        return null;
    }
}
