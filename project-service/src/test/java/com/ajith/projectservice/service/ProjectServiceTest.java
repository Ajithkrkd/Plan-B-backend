package com.ajith.projectservice.service;

import com.ajith.projectservice.dto.ProjectDetailsResponse;
import com.ajith.projectservice.dto.ProjectDetailsWithOutMembers;
import com.ajith.projectservice.dto.ProjectRequest;
import com.ajith.projectservice.entity.Project;
import com.ajith.projectservice.exceptions.ResourceAlreadyExist;
import com.ajith.projectservice.exceptions.UserNotFoundException;
import com.ajith.projectservice.feign.dto.User;
import com.ajith.projectservice.feign.service.UserServiceFeign;
import com.ajith.projectservice.repository.ProjectRepository;
import com.ajith.projectservice.utils.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

class ProjectServiceTest {

    @Mock
    ProjectRepository projectRepository;

    @Mock
    UserServiceFeign userServiceFeign;

    @Mock
    MapperService mapperService;


    @Mock
    JwtUtils jwtUtils;

    @InjectMocks
    ProjectService projectService;



    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.openMocks ( this );

    }

    @Test
    @DisplayName ( " this test is should create a project " )
    void createProject ( ) throws ResourceAlreadyExist {
        // given
        String authHeader = "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJVU0VSIl0sInN1YiI6ImFqaXRoMjI1NWl0aUBnbWFpbC5jb20iLCJpYXQiOjE3MTA5NDEyODEsImV4cCI6MTcxMTU0NjA4MX0.sKL2nbzcOfAFvuKzjtflhOhjFolSXlTC90XwdNh-rQE";
        String title = "pedal_planet";

        ProjectRequest projectRequest = new ProjectRequest (
                title,
                "This is a sample project"
        );

        User expectedUser = User.builder ( )
                .userId ( 123L )
                .email ( "test@example.com" )
                .fullName ( "test" )
                .isBlocked ( false )
                .isEmailVerified ( true )
                .joinDate ( new Date ( System.currentTimeMillis ( ) ) )
                .role ( null )
                .profile_image_path ( null )
                .build ( );

        Project createdProject = Project.builder ( )
                .id ( 124L )
                .title ( title )
                .description ( "this is a sample project" )
                .createdAt ( LocalDateTime.now ( ) )
                .projectAdministrators ( new ArrayList <> ( ) )
                .assignedMembersIds ( new ArrayList <> ( ) )
                .project_profile_url ( null )
                .projectRootAdministratorEmail ( "ajith@gmail.com" )
                .is_deleted ( false )
                .build ( );

        //when
        when ( userServiceFeign.getUserByAuthHeader ( authHeader ) ).thenReturn (ResponseEntity.ok(expectedUser));
        when ( projectRepository.existsByTitle ( title ) ).thenReturn ( false );
        when ( mapperService.mapProjectRequestToProject ( projectRequest, expectedUser ) ).thenReturn ( createdProject );
        when ( projectRepository.save ( createdProject ) ).thenReturn ( createdProject );

        projectService.createProject ( projectRequest, authHeader );

        //then
        assertThat ( createdProject ).isNotNull ( );
        assertThat ( createdProject.getId ( ) ).isGreaterThan ( 0 );
        assertThat ( createdProject.getTitle ( ) ).isEqualTo ( title );
        assertThat ( createdProject.getProjectRootAdministratorEmail ( ) ).isEqualTo ( "ajith@gmail.com" );

    }

    @Test
    void should_throw_UserNotFountException_when_expectedUser_is_not_get_from_userFeignService() throws UserNotFoundException{
        //given
            String authHeader = "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJVU0VSIl0sInN1YiI6ImFqaXRoMjI1NWl0aUBnbWFpbC5jb20iLCJpYXQiOjE3MTA5NDEyODEsImV4cCI6MTcxMTU0NjA4MX0.sKL2nbzcOfAFvuKzjtflhOhjFolSXlTC90XwdNh-rQE";
            String title = "pedal_planet";
            ProjectRequest projectRequest = new ProjectRequest (
                    title,
                    "This is a sample project"
            );
        //when
            when ( userServiceFeign.getUserByAuthHeader ( authHeader ) ).thenReturn ( ResponseEntity.noContent ().build () );
        //then
            assertThatThrownBy(() -> projectService.createProject(projectRequest, authHeader))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessageContaining("user not found");
    }

    @Test
    void should_return_ResourceAlreadyExistException_when_project_title_alreadyExist() throws UserNotFoundException{
        //given
            String authHeader = "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJVU0VSIl0sInN1YiI6ImFqaXRoMjI1NWl0aUBnbWFpbC5jb20iLCJpYXQiOjE3MTA5NDEyODEsImV4cCI6MTcxMTU0NjA4MX0.sKL2nbzcOfAFvuKzjtflhOhjFolSXlTC90XwdNh-rQE";
            String title = "name of project";
            ProjectRequest projectRequest = new ProjectRequest (
                    title,
                    "This is a sample project"
            );
            User expectedUser = User.builder ().userId ( 1233L ).build ();
        //when
            when ( userServiceFeign.getUserByAuthHeader ( authHeader ) ).thenReturn (ResponseEntity.ok(expectedUser));
            when ( projectRepository.existsByTitle ( title ) ).thenReturn ( true ) ;
        //then
            assertThatThrownBy(() -> projectService.createProject(projectRequest, authHeader))
                    .isInstanceOf(ResourceAlreadyExist.class)
                    .hasMessageContaining("project already exist with this title : name of project");
    }

    @Test
    @DisplayName ( "This method to get all projectDetails with project id project root admin email it will extract from auth header" )
    void get_project_details_with_project_root_admin_email() {
        //given
        String authHeader = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJVU0VSIl0sInN1YiI6ImFqaXRoMjI1NWl0aUBnbWFpbC5jb20iLCJpYXQiOjE3MTA5NDEyODEsImV4cCI6MTcxMTU0NjA4MX0.sKL2nbzcOfAFvuKzjtflhOhjFolSXlTC90XwdNh-rQE";
        String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJVU0VSIl0sInN1YiI6ImFqaXRoMjI1NWl0aUBnbWFpbC5jb20iLCJpYXQiOjE3MTA5NDEyODEsImV4cCI6MTcxMTU0NjA4MX0.sKL2nbzcOfAFvuKzjtflhOhjFolSXlTC90XwdNh-rQE";
        String userName = "ajith@gmail.com";

        Project project1 = Project.builder ( )
                .id ( 124L )
                .title ( "pedal new one" )
                .description ( "this is a sample project" )
                .createdAt ( LocalDateTime.now ( ) )
                .projectAdministrators ( new ArrayList <> ( ) )
                .assignedMembersIds ( new ArrayList <> ( ) )
                .project_profile_url ( null )
                .projectRootAdministratorEmail ( "ajith@gmail.com" )
                .is_deleted ( false )
                .build ( );
        Project project2 = Project.builder ( )
                .id ( 129L )
                .title ( "pedal2" )
                .description ( "this is a sample project 2" )
                .createdAt ( LocalDateTime.now ( ) )
                .projectAdministrators ( new ArrayList <> ( ) )
                .assignedMembersIds ( new ArrayList <> ( ) )
                .project_profile_url ( "/uploads/candle1.jpg" )
                .projectRootAdministratorEmail ( "ajith@gmail.com" )
                .is_deleted ( false )
                .build ( );

        ProjectDetailsWithOutMembers projectDetails1 = ProjectDetailsWithOutMembers
                .builder ( )
                .projectId ( project1.getId ( ) )
                .title ( project1.getTitle ( ) )
                .description ( project1.getDescription ( ) )
                .project_profile_url ( project1.getProject_profile_url ( ) )
                .build ( );
        ProjectDetailsWithOutMembers projectDetails2 = ProjectDetailsWithOutMembers
                .builder ( )
                .projectId ( project2.getId ( ) )
                .title ( project2.getTitle ( ) )
                .description ( project2.getDescription ( ) )
                .project_profile_url ( project2.getProject_profile_url ( ) )
                .build ( );

        List < Project > expectedList = Arrays.asList ( project1, project2 );
        List < ProjectDetailsWithOutMembers > expectedProjectDetailsList = Arrays.asList ( projectDetails1, projectDetails2 );

        //when
        when ( jwtUtils.getTokenFromAuthHeader ( authHeader ) ).thenReturn ( token );
        when ( jwtUtils.getUsernameFromToken ( token ) ).thenReturn ( userName );
        when ( projectRepository.findByProjectRootAdministratorEmail ( userName ) ).thenReturn ( expectedList );
        when ( mapperService.createProjectDetailsWithOutMembers ( project1 ) ).thenReturn ( projectDetails1 );
        when ( mapperService.createProjectDetailsWithOutMembers ( project2 ) ).thenReturn ( projectDetails2 );

        ResponseEntity < List < ProjectDetailsWithOutMembers > > projectDetailsList = projectService.getAllProjectDetails ( authHeader );

        List < ProjectDetailsWithOutMembers > resultList = projectDetailsList.getBody ( );
        //then
        assertThat ( resultList.size ( ) ).isEqualTo ( 2 );
        assertThat ( resultList.get ( 0 ).getProjectId ( ) ).isEqualTo ( project1.getId ( ) );
        assertThat ( resultList.get ( 1 ).getProjectId ( ) ).isEqualTo ( project2.getId ( ) );
        assertThat ( resultList.get ( 0 ).getTitle ( ) ).isEqualTo ( project1.getTitle ( ) );
        assertThat ( resultList.get ( 1 ).getTitle ( ) ).isEqualTo ( project2.getTitle ( ) );
    }

    @Test
    @DisplayName ( "This method should return the project details by the project id" )
    void getProjectByProjectId ( ) {
        //given
        Long projectId = 1203L;
        String authHeader = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJVU0VSIl0sInN1YiI6ImFqaXRoMjI1NWl0aUBnbWFpbC5jb20iLCJpYXQiOjE3MTA5NDEyODEsImV4cCI6MTcxMTU0NjA4MX0.sKL2nbzcOfAFvuKzjtflhOhjFolSXlTC90XwdNh-rQE";
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

        ProjectDetailsResponse projectDetailsResponse = ProjectDetailsResponse.builder ( )
                .projectId ( existingProject.getId () )
                .title ( existingProject.getTitle () )
                .description ( existingProject.getDescription () )
                .project_profile_url ( existingProject.getProject_profile_url () )
                .assignedMembersDetailsList ( new ArrayList <> (  ) )
                .projectAdministratorDetailsList ( new ArrayList <> (  ) )
                .projectRootAdministratorEmail ( "ajith@gmail.com" )
                .createdDateTime ( LocalDateTime.now (  ) )
                .build ( );
        //when
        when ( projectRepository.findById ( projectId ) ).thenReturn ( Optional.ofNullable ( existingProject ) );
        when ( mapperService.mapProjectToProjectDetailsResponse ( existingProject ,authHeader ) ).thenReturn ( projectDetailsResponse );

       ResponseEntity<ProjectDetailsResponse>  response = projectService.getProjectByProjectId (authHeader, projectId );
       ProjectDetailsResponse result = response.getBody ();
        //then

        assertThat ( result.getProjectId () ).isGreaterThan ( 0 );
        assertThat ( result.getTitle () ).isEqualTo (existingProject.getTitle ());
        assertThat ( result ).isNotNull ();
        assertThat ( result.getProjectRootAdministratorEmail () ).isEqualTo ( "ajith@gmail.com" );

    }

    @Test
    @DisplayName ( "should return project exist or not with the project id" )
    void isProjectExist ( ) {
        //given
        Long projectId = 1203L;

        //when
        when ( projectRepository.existsById ( projectId )).thenReturn ( false );
        boolean isExist = projectService.isProjectExist ( projectId );

        //then
        assertThat ( isExist ).isFalse ();
    }
}