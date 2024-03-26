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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class MemberInvitationServiceTest {

    @Mock
    private UserServiceFeign userServiceFeign;
    @Mock
    private EventService eventService;
    @Mock
    private InvitationRepository invitationRepository;
    @Mock
    private KafkaProducer kafkaProducer;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private ProjectDtoMapper projectDtoMapper;

    @InjectMocks
    private MemberInvitationService underTestService;

    @BeforeEach
    void setup ()throws Exception {
        MockitoAnnotations.openMocks ( this );
    }
    @Test
    void sentInviteToMember ( ) {
        //given
        InviteRequest inviteRequest = new InviteRequest ( "ajith@gmail.com" , "please join to out team" );
        String authHeader = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJVU0VSIl0sInN1YiI6ImFqaXRoMjI1NWl0aUBnbWFpbC5jb20iLCJpYXQiOjE3MTA5NDEyODEsImV4cCI6MTcxMTU0NjA4MX0.sKL2nbzcOfAFvuKzjtflhOhjFolSXlTC90XwdNh-rQE";

        String email ="ajith@gmail.com";
        String token = UUID.randomUUID ().toString ();

        InviteMemberEvent event =  InviteMemberEvent.builder ()
                .email ( email )
                .token ( token )
                .message ( inviteRequest.getMessage () )
                .expirationTime ( new Date ( System.currentTimeMillis () ) )
                .build ();
        Project existingProject = Project.builder ( )
                .id ( 1203L )
                .title ( "pedal2" )
                .description ( "this is a sample project 2" )
                .createdAt ( LocalDateTime.now ( ) )
                .projectAdministrators ( new ArrayList <> ( ) )
                .assignedMembersIds ( new ArrayList <> ( ) )
                .project_profile_url ( "/uploads/candle1.jpg" )
                .projectRootAdministratorEmail ( "ajith@gmail.com" )
                .is_deleted ( false )
                .build ( );
        User expectedUser = User.builder ().userId ( 1233L ).build ();
        //when
        when (userServiceFeign.getUserByAuthHeader ( authHeader )).thenReturn ( ResponseEntity.ok(expectedUser) );
        when(eventService.createInviteMemberEvent ( inviteRequest )).thenReturn ( event );
        ResponseEntity< BasicResponse > response = underTestService.sentInviteToMember ( inviteRequest,authHeader,existingProject);

        //then
        assertEquals (200, response.getBody ().getStatus () );
    }

    @Test
    public void testCreateInvitation_PrivateMethod() throws Exception {

        // Given (arrange)
        String email = "ajith@gmail.com";
        String token = UUID.randomUUID().toString();

        InviteMemberEvent event = InviteMemberEvent.builder()
                .email(email)
                .token(token)
                .message("This is an invitation to join the project.") // Informative message
                .expirationTime(new Date(System.currentTimeMillis()))
                .build();

        Project existingProject = Project.builder()
                .id(12032L)
                .title("pedal2")
                .description("This is a sample project 2")
                .createdAt(LocalDateTime.now())
                .projectAdministrators(new ArrayList<>())
                .assignedMembersIds(new ArrayList<>())
                .project_profile_url("/uploads/candle1.jpg")
                .projectRootAdministratorEmail("ajith@gmail.com")
                .is_deleted(false)
                .build();

        User expectedUser = User.builder().userId(1233L).email(email).build();
        // When (act)
        Method methodCall = underTestService.getClass ( ).getDeclaredMethod ( "createInvitation", InviteMemberEvent.class, Project.class, User.class);
        methodCall.setAccessible ( true );

        MemberInvitation actualInvitation = (MemberInvitation) methodCall.invoke ( underTestService,event,existingProject,expectedUser );

        // Then (assert)
        assertNotNull(actualInvitation);
        assertTrue ( actualInvitation.getInviteSentTo () == "ajith@gmail.com" );
        assertEquals ( expectedUser.getEmail () ,actualInvitation.getInviteSentTo () );

    }

    @Test
    @DisplayName ( "this method will accept the invitation request form the user" )
    void should_accept_invitation(){
        //given
        String invitationToken = UUID.randomUUID ().toString ();
        String authHeader = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJVU0VSIl0sInN1YiI6ImFqaXRoMjI1NWl0aUBnbWFpbC5jb20iLCJpYXQiOjE3MTA5NDEyODEsImV4cCI6MTcxMTU0NjA4MX0.sKL2nbzcOfAFvuKzjtflhOhjFolSXlTC90XwdNh-rQE";
        User expectedInvitedMember = User.builder().userId(123322L).email("ajith@gmail.com").build();

        List<Long> members = new ArrayList <> (  );
        members.add(12333333L);
        Project existingProject = Project.builder()
                .id(12032L)
                .title("pedal2")
                .description("This is a sample project 2")
                .createdAt(LocalDateTime.now())
                .projectAdministrators(new ArrayList<>())
                .assignedMembersIds(members)
                .project_profile_url("/uploads/candle1.jpg")
                .projectRootAdministratorEmail("ajithprakash.work@gmail.com")
                .is_deleted(false)
                .build();
        MemberInvitation expectedInvitation = MemberInvitation.builder ()
                .invitationToken ( invitationToken )
                .id ( 1242L )
                .invitationMessage ( "please join our team to help this project" )
                .inviteFrom ( "ajithprakash.work@gmail.com" )
                .inviteSentTo ("ajith@gmail.com")
                .project ( existingProject )
                .status ( InvitationStatus.PENDING )
                .build ();

        MemberInvitation updatedInvitation = MemberInvitation.builder ()
                .invitationToken ( invitationToken )
                .id ( 1242L )
                .invitationMessage ( "please join our team to help this project" )
                .inviteFrom ( "ajithprakash.work@gmail.com" )
                .inviteSentTo ("ajith@gmail.com")
                .project ( existingProject )
                .status ( InvitationStatus.ACCEPTED )
                .build ();
        //when
        when ( userServiceFeign.getUserByAuthHeader ( authHeader ) ).thenReturn ( ResponseEntity.ok ( expectedInvitedMember ) );
        when ( invitationRepository.findByInvitationToken ( invitationToken ) ).thenReturn ( Optional.ofNullable ( expectedInvitation ) );
        when ( projectRepository.findById ( existingProject.getId ( ) )).thenReturn ( Optional.of ( existingProject ) );
        when ( invitationRepository.save ( updatedInvitation ) ).thenReturn ( updatedInvitation );

        underTestService.acceptTheInviteRequest ( invitationToken,authHeader );
        //then
        assertEquals ( InvitationStatus.ACCEPTED , updatedInvitation.getStatus () );
        assertEquals ( invitationToken , expectedInvitation.getInvitationToken () );
        assertEquals (2,existingProject.getAssignedMembersIds ().size ());
        assertNotNull ( updatedInvitation.getId () );
        assertEquals ( updatedInvitation.getId (), expectedInvitation.getId () );
        assertEquals ( 123322L , existingProject.getAssignedMembersIds ().get ( 1 ) );
    }

    @Test
    void should_throw_ResourceNotFoundException_when_invitation_not_found_with_token() throws ResourceNotFoundException{
        //given
        String invitationToken = UUID.randomUUID ().toString ();
        String authHeader = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJVU0VSIl0sInN1YiI6ImFqaXRoMjI1NWl0aUBnbWFpbC5jb20iLCJpYXQiOjE3MTA5NDEyODEsImV4cCI6MTcxMTU0NjA4MX0.sKL2nbzcOfAFvuKzjtflhOhjFolSXlTC90XwdNh-rQE";
        //when
        when ( userServiceFeign.getUserByAuthHeader ( authHeader ) ).thenReturn ( ResponseEntity.ok ( new User (  ) ) );
        when(invitationRepository.findByInvitationToken ( invitationToken )).thenReturn ( Optional.empty () );
        //then
        assertThatThrownBy (()->underTestService.acceptTheInviteRequest ( invitationToken,authHeader ) )
                .isInstanceOf ( ResourceNotFoundException.class )
                .hasMessageContaining ( "The Invitation not fount with this token  "+invitationToken );
    }



    @Test
    @DisplayName ( "should return the UserNotFountException when the user getting from the authHeader is null" )
    void should_return_UserNotFountException_when_the_user_is_Null() throws  UserNotFoundException{
        //given
        String authHeader = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJVU0VSIl0sInN1YiI6ImFqaXRoMjI1NWl0aUBnbWFpbC5jb20iLCJpYXQiOjE3MTA5NDEyODEsImV4cCI6MTcxMTU0NjA4MX0.sKL2nbzcOfAFvuKzjtflhOhjFolSXlTC90XwdNh-rQE";
        //when
        when ( userServiceFeign.getUserByAuthHeader ( authHeader ) ).thenReturn (ResponseEntity.noContent ().build ());
        //then
        assertThatThrownBy ( ()->underTestService.getAllInvitations ( authHeader ) )
                .isInstanceOf ( UserNotFoundException.class )
                .hasMessageContaining ( "User not found with this authHeader" );
    }

    @Test
    void testGetAllInvitations5 ( ) {
        // Arrange

        User buildResult = User.builder ( ).email ( "jane.doe@example.org" ).fullName ( "Dr Jane Doe" )
                .joinDate (new Date ( System.currentTimeMillis () ) )
                .phoneNumber ( "6625550144" )
                .profile_image_path ( "Profile image path" )
                .role ( "Role" )
                .userId ( 1L )
                .build ( );
        when ( userServiceFeign.getUserByAuthHeader ( Mockito. < String >any ( ) ) ).thenReturn ( ResponseEntity.ok ( buildResult ) );

        Project project = new Project ( );
        project.setAssignedMembersIds ( new ArrayList <> ( ) );
        project.setCreatedAt ( LocalDate.of ( 1970, 1, 1 ).atStartOfDay ( ) );
        project.setDescription ( "The characteristics of someone or something" );
        project.setId ( 1L );
        project.setProjectAdministrators ( new ArrayList <> ( ) );
        project.setProjectRootAdministratorEmail ( "jane.doe@example.org" );
        project.setProject_profile_url ( "https://example.org/example" );
        project.setTitle ( "Dr" );
        project.set_deleted ( false );

        MemberInvitation memberInvitation = new MemberInvitation ( );
        memberInvitation
                .setCreatedAt ( Date.from ( LocalDate.of ( 1970, 1, 1 ).atStartOfDay ( ).atZone ( ZoneOffset.UTC ).toInstant ( ) ) );
        memberInvitation
                .setExpiryDate ( Date.from ( LocalDate.of ( 1970, 1, 1 ).atStartOfDay ( ).atZone ( ZoneOffset.UTC ).toInstant ( ) ) );
        memberInvitation.setId ( 1L );
        memberInvitation.setInvitationMessage ( "Invitation Message" );
        memberInvitation.setInvitationToken ( "ABC123" );
        memberInvitation.setInviteFrom ( "jane.doe@example.org" );
        memberInvitation.setInviteSentTo ( "alice.liddell@example.org" );
        memberInvitation.setProject ( project );
        memberInvitation.setStatus ( InvitationStatus.PENDING );

        ArrayList < MemberInvitation > memberInvitationList = new ArrayList <> ( );
        memberInvitationList.add ( memberInvitation );

        when ( invitationRepository.findAllByInviteSentTo ( Mockito. < String >any ( ) ) ).thenReturn ( memberInvitationList );

        MemberInvitationResponse.MemberInvitationResponseBuilder builderResult = MemberInvitationResponse.builder ( );
        MemberInvitationResponse.MemberInvitationResponseBuilder invitation_messageResult = builderResult
                .invitation_expires_time ( Date.from ( LocalDate.of ( 1970, 1, 1 ).atStartOfDay ( ).atZone ( ZoneOffset.UTC ).toInstant ( ) ) )
                .invitation_from ( "jane.doe@example.org" )
                .invitation_id ( 1L )
                .invitation_message ( "Invitation message" );

        MemberInvitationResponse buildResult2 = invitation_messageResult
                .invitation_sent_time ( Date.from ( LocalDate.of ( 1970, 1, 1 ).atStartOfDay ( ).atZone ( ZoneOffset.UTC ).toInstant ( ) ) )
                .invitation_status ( "Invitation status" )
                .invitation_token ( "ABC123" )
                .project_image_url ( "https://example.org/example" )
                .project_title ( "Dr" )
                .build ( );
        when ( projectDtoMapper.mapInvitationToInvitationResponse ( Mockito. < MemberInvitation >any ( ) ) ).thenReturn ( buildResult2 );

        // Act
        ResponseEntity < List < MemberInvitationResponse > > actualAllInvitations = underTestService
                .getAllInvitations ( "Auth Header" );

        // Assert
        verify ( userServiceFeign ).getUserByAuthHeader ( eq ( "Auth Header" ) );
        verify ( projectDtoMapper ).mapInvitationToInvitationResponse ( isA ( MemberInvitation.class ) );
        verify ( invitationRepository ).findAllByInviteSentTo ( eq ( "jane.doe@example.org" ) );
        assertEquals ( 200, actualAllInvitations.getStatusCodeValue ( ) );
        assertTrue ( actualAllInvitations.hasBody ( ) );
        assertTrue ( actualAllInvitations.getHeaders ( ).isEmpty ( ) );
    }

    @Test
    @DisplayName ( "should return the empty list when the user have not invitations" )
    void should_return_empty_listOfInvitations_when_user_has_no_invitations() {
        //given
        String authHeader = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJVU0VSIl0sInN1YiI6ImFqaXRoMjI1NWl0aUBnbWFpbC5jb20iLCJpYXQiOjE3MTA5NDEyODEsImV4cCI6MTcxMTU0NjA4MX0.sKL2nbzcOfAFvuKzjtflhOhjFolSXlTC90XwdNh-rQE";
        User expectedInvitedMember = User.builder().userId(123322L).email("ajith@gmail.com").build();
        //when
        when ( userServiceFeign.getUserByAuthHeader ( authHeader ) ).thenReturn ( ResponseEntity.ok ( expectedInvitedMember ) );
        when ( invitationRepository.findAllByInviteSentTo ( expectedInvitedMember.getEmail () )).thenReturn ( new ArrayList <> (  )  );

        ResponseEntity<List< MemberInvitationResponse >> resultList = underTestService.getAllInvitations ( authHeader );

        //then
        assertEquals ( HttpStatus.NO_CONTENT,resultList.getStatusCode () );
        assertEquals (0, resultList.getBody ().size ( ));
    }
}