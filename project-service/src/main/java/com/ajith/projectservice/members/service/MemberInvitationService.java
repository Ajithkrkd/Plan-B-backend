package com.ajith.projectservice.members.service;

import com.ajith.projectservice.entity.Project;
import com.ajith.projectservice.exceptions.ResourceNotFoundException;
import com.ajith.projectservice.exceptions.UserNotFoundException;
import com.ajith.projectservice.feign.dto.User;
import com.ajith.projectservice.feign.service.UserServiceFeign;
import com.ajith.projectservice.kafka.event.InviteMemberEvent;
import com.ajith.projectservice.kafka.service.EventService;
import com.ajith.projectservice.kafka.service.KafkaProducer;
import com.ajith.projectservice.members.dto.InviteRequest;
import com.ajith.projectservice.members.dto.MemberInvitationResponse;
import com.ajith.projectservice.members.entity.MemberInvitation;
import com.ajith.projectservice.members.enums.InvitationStatus;
import com.ajith.projectservice.members.mappers.ProjectDtoMapper;
import com.ajith.projectservice.members.repository.InvitationRepository;
import com.ajith.projectservice.repository.ProjectRepository;
import com.ajith.projectservice.utils.BasicResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberInvitationService implements IMemberInvitationService{
    private final UserServiceFeign userServiceFeign;
    private  final EventService eventService;
    private final InvitationRepository invitationRepository;
    private final KafkaProducer kafkaProducer;
    private final ProjectRepository projectRepository;
    private final ProjectDtoMapper projectDtoMapper;
    @Override
    public ResponseEntity < BasicResponse > sentInviteToMember (InviteRequest inviteRequest, String authHeader, Project project) {

        try{
            User invitedUser = userServiceFeign.getUserByAuthHeader ( authHeader ).getBody ();

            InviteMemberEvent event = eventService.createInviteMemberEvent ( inviteRequest );
            kafkaProducer.sentMessage ( event );
            saveInvitationDetailsToDatabase (event ,project,invitedUser);
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

    @Override
    public ResponseEntity < BasicResponse > acceptTheInviteRequest (String token, String authHeader) {
        try{
            User expectedUser = userServiceFeign.getUserByAuthHeader ( authHeader ).getBody ();
            Optional<MemberInvitation> invitation = invitationRepository.findByInvitationToken ( token );
            if (!invitation.isPresent()){
                throw new ResourceNotFoundException ( "The Invitation not fount with this token  "+token );
            }
            String invitationReceiverEmail = invitation.get().getInviteSentTo ();
            MemberInvitation invitationWantToUpdate = invitation.get ();
            Optional < Project > invitedProject = projectRepository.findById ( invitationWantToUpdate.getProject ().getId ());
            Project project = invitedProject.get ();
            project.getAssignedMembersIds ().add ( expectedUser.getUserId () );
            projectRepository.save ( project );

            if(invitationReceiverEmail.equals ( expectedUser.getEmail () )){

                invitationWantToUpdate.setStatus ( InvitationStatus.ACCEPTED );
              invitationRepository.save ( invitationWantToUpdate );
            }
            return ResponseEntity.ok ( BasicResponse.builder ()
                    .message ( "invite accepted successfully" )
                    .status ( HttpStatus.OK.value ( ) )
                    .description ( "you are now member of this project  " + project.getTitle () )
                    .timestamp ( LocalDateTime.now () )
                    .build ());

        }
        catch (ResourceNotFoundException e) {
            throw new RuntimeException ( e.getMessage () );
        }
        catch (Exception e){
            throw new RuntimeException ( e.getMessage () );
        }
    }

    @Override
    public ResponseEntity < List< MemberInvitationResponse > > getAllInvitations (String authHeader) {
        try {
            User currentUser = userServiceFeign.getUserByAuthHeader ( authHeader ).getBody ();
            if(currentUser == null){
                throw new UserNotFoundException ( "User not found with this authHeader" );
            }

            List <MemberInvitation> invitationsList = invitationRepository.findAllByInviteSentTo (currentUser.getEmail ());
            if(invitationsList.isEmpty () ){
                return new ResponseEntity <> ( Collections.emptyList ( ),HttpStatus.NO_CONTENT );
            }

         List<MemberInvitationResponse> responsesList =  invitationsList
                 .stream ()
                 .map ( invitation -> projectDtoMapper.mapInvitationToInvitationResponse(invitation) )
                 .collect( Collectors.toList());
        return ResponseEntity.ok ( responsesList );
        }catch (UserNotFoundException e){
            throw new RuntimeException ( e.getMessage () );
        }
        catch (Exception e){
            throw new RuntimeException ( e.getMessage () );
        }
    }
    private void saveInvitationDetailsToDatabase (InviteMemberEvent event, Project project,User invitedUser) {
      MemberInvitation memberInvitation =   MemberInvitation.builder ()
                .invitationToken ( event.getToken ( ) )
                .inviteFrom ( invitedUser.getEmail () )
                .createdAt ( new Date ( System.currentTimeMillis () ) )
                .expiryDate ( new Date (System.currentTimeMillis () + 604800000) )
                .inviteSentTo ( event.getEmail ( ) )
                .invitationMessage ( event.getMessage ( ) )
                .status ( InvitationStatus.PENDING )
                .project ( project )
                .build ();
        invitationRepository.save ( memberInvitation );
    }
}
