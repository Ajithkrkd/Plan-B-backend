package com.ajith.projectservice.members.controller;

import com.ajith.projectservice.entity.Project;
import com.ajith.projectservice.feign.dto.User;
import com.ajith.projectservice.feign.service.UserServiceFeign;
import com.ajith.projectservice.members.dto.InviteRequest;
import com.ajith.projectservice.members.dto.MemberInvitationResponse;
import com.ajith.projectservice.members.entity.MemberInvitation;
import com.ajith.projectservice.members.enums.InvitationStatus;
import com.ajith.projectservice.members.repository.InvitationRepository;
import com.ajith.projectservice.members.service.IMemberInvitationService;
import com.ajith.projectservice.repository.ProjectRepository;
import com.ajith.projectservice.utils.BasicResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = MembersInvitationController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith ( MockitoExtension.class )
class MembersInvitationControllerTest {

    @Autowired
    public MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    ProjectRepository projectRepository;
    @MockBean
    IMemberInvitationService memberInvitationService;

    @MockBean
    UserServiceFeign userServiceFeign;

    @MockBean
    InvitationRepository invitationRepository;

    private InviteRequest inviteRequest;
    private Project project;

    @BeforeEach
    void init ( ) {
        inviteRequest = new InviteRequest("ajith@gmail.com", "please do join our project");
        project = Project.builder ().id ( 12L ).title ( "sample-project" ).description ( "this is a sample project " )
                .projectRootAdministratorEmail ( "admin@gmail.com" ).projectAdministrators ( new ArrayList <> (  ) )
                .assignedMembersIds ( new ArrayList<> () ).project_profile_url ( null ).createdAt ( LocalDateTime.now () ).is_deleted ( false ).build ();
    }

    @Test
    public void should_return_bad_request_when_invite_request_missing_fields() throws Exception {
        // Missing email
        InviteRequest inviteRequest = new InviteRequest();
        inviteRequest.setMessage("This is a sample message");

        ResultActions response = mockMvc.perform(post("/project/members/invite/12")
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9...")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inviteRequest)));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Your invite request missing something")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", CoreMatchers.is("nullThis is a sample message")));
    }

    @Test
    public void sentInvitationForJoiningToProject ( ) throws Exception {
        //given
        String authHeader = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJVU0VSIl0sInN1YiI6ImFqaXRoMjI1NWl0aUBnbWFpbC5jb20iLCJpYXQiOjE3MTA5NDEyODEsImV4cCI6MTcxMTU0NjA4MX0.sKL2nbzcOfAFvuKzjtflhOhjFolSXlTC90XwdNh-rQE";
        BasicResponse inviteSentSuccessfully = BasicResponse.builder()
                .status ( HttpStatus.OK.value ( ) )
                .timestamp ( LocalDateTime.now () )
                .message ( "Invite sent successfully" )
                .description ( "we have sent a invite to the member "+ inviteRequest.getEmail () )
                .build();
        given(memberInvitationService.sentInviteToMember ( any ( InviteRequest.class),any (String.class),any (Project.class)))
                .willReturn (ResponseEntity.status ( HttpStatus.OK ).body (inviteSentSuccessfully));
        //when
        when ( projectRepository.findById ( project.getId () ) ).thenReturn ( Optional.ofNullable ( project ) );

        ResultActions response = mockMvc.perform ( post("/project/members/invite/12")
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9...")
                .contentType ( MediaType.APPLICATION_JSON )
                .content ( objectMapper.writeValueAsString ( inviteRequest ) ));
        //then
        response.andExpect ( MockMvcResultMatchers.status ().isOk () )
                .andExpect ( MockMvcResultMatchers.jsonPath ( "$.message", CoreMatchers.is ( "Invite sent successfully" ) ))
                .andExpect ( MockMvcResultMatchers.jsonPath ( "$.status" ,CoreMatchers.is ( 200 ) ) )
                .andExpect ( MockMvcResultMatchers.jsonPath ( "$.description",CoreMatchers.is ( "we have sent a invite to the member "+ inviteRequest.getEmail () ) ) );
    }
    @Test
    public void should_return_bad_request_when_project_not_found() throws Exception {
        InviteRequest inviteRequest = new InviteRequest();
        inviteRequest.setEmail("test@example.com");
        inviteRequest.setMessage("This is a sample message");

        when(projectRepository.findById(123L)).thenReturn(Optional.empty());

        ResultActions response = mockMvc.perform(post("/project/members/invite/123")
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9...")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inviteRequest)));

        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("project does not exist with given project id")));
    }


    //accept invite method
    @Test
    public void should_return_not_found_if_invitation_not_found() throws Exception {
        String token = "test-token";
        String authHeader = "Bearer eyJhbGciOiJIUzI1NiJ9...";

        BasicResponse badResponse = BasicResponse.builder()
                .status ( HttpStatus.NOT_FOUND.value ( ) )
                .timestamp ( LocalDateTime.now () )
                .message ( "The Invitation not fount with this token  "+token )
                .description ( " unable to find the user check end point request others" )
                .build();
        given(memberInvitationService.acceptTheInviteRequest ( any ( String.class),any (String.class)))
                .willReturn (ResponseEntity.status ( HttpStatus.NOT_FOUND ).body (badResponse));

//        User user = new User();
//        when(userServiceFeign.getUserByAuthHeader(authHeader)).thenReturn(ResponseEntity.ok(user));
//        when(invitationRepository.findByInvitationToken(token)).thenReturn(Optional.empty());
        ResultActions response = mockMvc.perform(post("/project/members/accept")
                .header("Authorization", authHeader)
                .param("token", token));
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect ( MockMvcResultMatchers.status().isNotFound() )
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("The Invitation not fount with this token  test-token")));
    }

    @Test
    public void should_accept_invitation_and_update_project_membership() throws Exception {
        String token = "test-token";
        String authHeader = "Bearer eyJhbGciOiJIUzI1NiJ9...";

        MemberInvitation invitation = MemberInvitation.builder ( ).status ( InvitationStatus.PENDING ).id ( 123456L ).inviteSentTo ( "ajith@gmail.com" )
                .invitationToken ( "test-token" ).inviteFrom ( "ajithFrom@gmail.com" ).project ( project ).build ( );

        BasicResponse successResponse = BasicResponse.builder ()
                .message ( "invite accepted successfully" )
                .status ( HttpStatus.OK.value ( ) )
                .description ( "you are now member of this project  " + project.getTitle () )
                .timestamp ( LocalDateTime.now () )
                .build ();
        given(memberInvitationService.acceptTheInviteRequest ( any ( String.class),any (String.class)))
                .willReturn (ResponseEntity.status ( HttpStatus.OK ).body (successResponse));

//        User user = User.builder ().userId ( 2222L ).email ( "ajith@gmail.com" ).fullName ( "Ajith" ).build ();
//        when(userServiceFeign.getUserByAuthHeader(authHeader)).thenReturn(ResponseEntity.ok(user));
//        when(invitationRepository.findByInvitationToken(token)).thenReturn( Optional.ofNullable ( invitation ) );
//        when ( projectRepository.findById ( project.getId () ) ).thenReturn( Optional.of(project));
//        when ( memberInvitationService.acceptTheInviteRequest ( token,authHeader ) ).thenReturn ( ResponseEntity.status ( HttpStatus.OK ).body ( successResponse ) );

        ResultActions response = mockMvc.perform(post("/project/members/accept")
                .header("Authorization", authHeader)
                .param("token", token));
        response.andExpect(MockMvcResultMatchers.status().isOk ())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("invite accepted successfully")))
                .andExpect ( MockMvcResultMatchers.jsonPath ( "$.status" ,CoreMatchers.is ( 200 ) ) )
                .andExpect ( MockMvcResultMatchers.jsonPath ( "$.description",CoreMatchers.is ( "you are now member of this project  " + project.getTitle ()  ) ) );
    }


    //get all invitations
    @Test
    public void should_return_no_content_if_no_invitations_found() throws Exception {
        String authHeader = "Bearer eyJhbGciOiJIUzI1NiJ9...";
        User user = new User();
        user.setEmail("test@example.com");
        given(memberInvitationService.getAllInvitations ( any ( String.class)))
                .willReturn (ResponseEntity.status ( HttpStatus.NO_CONTENT ).body ( Collections.emptyList () ));

        ResultActions response = mockMvc.perform(get ("/project/members/invitations")
                .header("Authorization", authHeader));

        response.andExpect(MockMvcResultMatchers.status().isNoContent());
    }
    @Test
    public void should_return_list_of_invitation_responses_for_user() throws Exception {
        String authHeader = "Bearer eyJhbGciOiJIUzI1NiJ9...";
        User user = new User();
        user.setEmail("test@example.com");
        String invitationToken = "test-token";
        List < MemberInvitationResponse > memberInvitationResponseList = new ArrayList<>();
        MemberInvitationResponse memberInvitationResponse = MemberInvitationResponse
                .builder ().invitation_id ( 1234L ).invitation_from ( "ajithFrom@gmail.com" ).invitation_message ( "please join" )
                        .invitation_status ( String.valueOf ( InvitationStatus.ACCEPTED ) ).invitation_sent_time ( new Date (System.currentTimeMillis ()) ).invitation_token ( invitationToken ).build ();
       memberInvitationResponseList.add ( memberInvitationResponse);
        given(memberInvitationService.getAllInvitations ( any ( String.class)))
                .willReturn (ResponseEntity.status ( HttpStatus.OK ).body ( memberInvitationResponseList));

        ResultActions response = mockMvc.perform(get ("/project/members/invitations")
                .header("Authorization", authHeader));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect ( MockMvcResultMatchers.jsonPath ( "$[0]invitation_id" ,CoreMatchers.is ( 1234 )) )
                .andExpect ( MockMvcResultMatchers.jsonPath ( "$[0]invitation_from" ,CoreMatchers.is ( "ajithFrom@gmail.com" )) )
                .andExpect ( MockMvcResultMatchers.jsonPath ( "$[0]invitation_status" ,CoreMatchers.is ( "ACCEPTED" )) )
                .andExpect ( MockMvcResultMatchers.jsonPath ( "$[0]invitation_message" ,CoreMatchers.is ( "please join" )) )
                .andExpect ( MockMvcResultMatchers.jsonPath ( "$[0]invitation_token" ,CoreMatchers.is ( "test-token" )) )
                .andDo ( MockMvcResultHandlers.print () );
    }
}