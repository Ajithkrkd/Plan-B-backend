package com.ajith.projectservice.members.service;

import com.ajith.projectservice.entity.Project;
import com.ajith.projectservice.kafka.event.InviteMemberEvent;
import com.ajith.projectservice.kafka.service.EventService;
import com.ajith.projectservice.kafka.service.KafkaProducer;
import com.ajith.projectservice.members.dto.InviteRequest;
import com.ajith.projectservice.members.entity.MemberInvitation;
import com.ajith.projectservice.members.enums.InvitationStatus;
import com.ajith.projectservice.members.repository.InvitationRepository;
import com.ajith.projectservice.utils.BasicResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class MemberInvitationService implements IMemberInvitationService{

    private  final EventService eventService;
    private final InvitationRepository invitationRepository;
    private final KafkaProducer kafkaProducer;
    @Override
    public ResponseEntity < BasicResponse > sentInviteToMember (InviteRequest inviteRequest, String authHeader, Project project) {

        try{
            InviteMemberEvent event = eventService.createInviteMemberEvent ( inviteRequest );
            kafkaProducer.sentMessage ( event );
            saveInvitationDetailsToDatabase (event ,project );
            return ResponseEntity.ok ( BasicResponse.builder()
                            .status ( HttpStatus.OK.value ( ) )
                            .timestamp ( LocalDateTime.now () )
                            .message ( "Invite sent successfully" )
                            .description ( "we have sent a invite to the member "+ inviteRequest.getEmail () )
                    .build() );
        }catch (Exception e){
            throw new RuntimeException ( e.getMessage () );
        }

    }

    private void saveInvitationDetailsToDatabase (InviteMemberEvent event, Project project) {
      MemberInvitation memberInvitation =   MemberInvitation.builder ()
                .invitationToken ( event.getToken ( ) )
                .createdAt ( new Date ( System.currentTimeMillis () ) )
                .expiryDate ( new Date (System.currentTimeMillis () + 604800000) )
                .memberEmail ( event.getEmail ( ) )
                .invitationMessage ( event.getMessage ( ) )
                .status ( InvitationStatus.PENDING )
                .project ( project )
                .build ();
        invitationRepository.save ( memberInvitation );
    }
}
