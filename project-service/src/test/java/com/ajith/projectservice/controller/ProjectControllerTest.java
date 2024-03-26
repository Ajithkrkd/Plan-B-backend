package com.ajith.projectservice.controller;

import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.ajith.projectservice.dto.ProjectDetailsResponse;
import com.ajith.projectservice.dto.ProjectDetailsWithOutMembers;
import com.ajith.projectservice.dto.ProjectRequest;
import com.ajith.projectservice.exceptions.ResourceAlreadyExist;
import com.ajith.projectservice.service.IProjectService;
import com.ajith.projectservice.service.ProjectService;
import com.ajith.projectservice.utils.BasicResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

@WebMvcTest(controllers = ProjectController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith ( MockitoExtension.class )
class ProjectControllerTest {

    @Autowired
    public MockMvc mockMvc;

    @MockBean
    ProjectService projectService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProjectRequest projectRequest;
    @BeforeEach
    public void init(){
        MockitoAnnotations.openMocks ( this );
         projectRequest = ProjectRequest.builder ().title ( "pedal" ).description ( "pedal planet description" ).build ();
    }

    @Test
    void testCreateProject ( ) throws ResourceAlreadyExist, Exception {
        // Arrange
        // Mock the service to return a successful response with a BasicResponse object
        BasicResponse expectedResponse = BasicResponse.builder()
                .message("Project created successfully")
                .description("...") // Add relevant details
                .status( HttpStatus.CREATED.value())
                .build();
        given(projectService.createProject(Mockito.any(ProjectRequest.class), Mockito.anyString()))
                .willReturn(ResponseEntity.status (HttpStatus.CREATED).body (expectedResponse));

        ResultActions response = mockMvc.perform ( post("/project/create")
                .header ( "Authorization" ,"Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJVU0VSIl0sInN1YiI6ImFqaXRoMjI1NWl0aUBnbWFpbC5jb20iLCJpYXQiOjE3MTA5NDEyODEsImV4cCI6MTcxMTU0NjA4MX0.sKL2nbzcOfAFvuKzjtflhOhjFolSXlTC90XwdNh-rQE")
                .contentType ( MediaType.APPLICATION_JSON )
                .content ( objectMapper.writeValueAsString ( projectRequest ) ));

        // Act and Assert
         response.andExpect ( MockMvcResultMatchers.status ().isCreated () )
                 .andExpect ( MockMvcResultMatchers.jsonPath ( "$.message", CoreMatchers.is ( "Project created successfully" ) ));

    }
    @Test
    void should_return_all_projects_by_authHeader ( ) throws ResourceAlreadyExist, Exception {
        // Arrange/given

        List < ProjectDetailsWithOutMembers > projectList = new ArrayList <> (  );
        ProjectDetailsWithOutMembers projectDetails = ProjectDetailsWithOutMembers.builder ( )
                .projectId ( 123L )
                .project_profile_url ( "/uploads/candle1.jpg" )
                .description ( "this is a sample project" )
                .title ( "pedal planet" )
                .build ( );
        projectList.add ( projectDetails );

        given(projectService.getAllProjectDetails (Mockito.anyString()))
                .willReturn(ResponseEntity.status (HttpStatus.OK).body (projectList));

        ResultActions response = mockMvc.perform ( get ("/project/get_all_projects")
                .header ( "Authorization" ,"Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJVU0VSIl0sInN1YiI6ImFqaXRoMjI1NWl0aUBnbWFpbC5jb20iLCJpYXQiOjE3MTA5NDEyODEsImV4cCI6MTcxMTU0NjA4MX0.sKL2nbzcOfAFvuKzjtflhOhjFolSXlTC90XwdNh-rQE")
                .contentType ( MediaType.APPLICATION_JSON ));

        // Act and Assert
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))  // Content type assertion
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].projectId").value(projectDetails.getProjectId().intValue()))  // Assert project ID
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value(projectDetails.getTitle()))  // Assert title
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value(projectDetails.getDescription()))  // Assert description
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].project_profile_url").value(projectDetails.getProject_profile_url()));  // response.andExpect ( MockMvcResultMatchers.status ().isOk () )


    }
}
