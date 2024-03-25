package com.ajith.projectservice.repository;

import com.ajith.projectservice.dto.ProjectRequest;
import com.ajith.projectservice.entity.Project;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;

    @AfterEach
    void tearDown() throws Exception {
        projectRepository.deleteAll ();
    }

    @Test
    @DisplayName ( value = "should return true because project title is duplicated")
    void shouldReturnTrueWhenProjectTitleIsAlreadyExist ( ) {
        //given
        ProjectRequest projectRequest = new ProjectRequest(
                "home page",
                "this is a sample project");

        Project alreadyExistsProject = Project.builder ()
                .title ( "home page" )
                .description ( "This is a sample project" )
                .projectAdministrators ( new ArrayList <> ( ) )
                .project_profile_url ( null )
                .assignedMembersIds ( new ArrayList <> (  ) )
                .projectRootAdministratorEmail ( "ajith@gmail.com" )
                .createdAt ( LocalDateTime.now () )
                .is_deleted ( false )
                .build ();
        projectRepository.save ( alreadyExistsProject );

        //when
        boolean isExistByTitle = projectRepository.existsByTitle ( projectRequest.getTitle ( ) );
        System.out.println (isExistByTitle );
        //then
        assertTrue ( isExistByTitle );
    }

    @Test
    void shouldReturnFalseWhenProjectTitleIsDoesNotExist ( ) {
        //given
        ProjectRequest projectRequest = new ProjectRequest(
                "home page",
                "this is a sample project");

        Project alreadyExistsProject = Project.builder ()
                .title ( "home" )
                .description ( "This is a sample project" )
                .projectAdministrators ( new ArrayList <> ( ) )
                .project_profile_url ( null )
                .assignedMembersIds ( new ArrayList <> (  ) )
                .projectRootAdministratorEmail ( "ajith@gmail.com" )
                .createdAt ( LocalDateTime.now () )
                .is_deleted ( false )
                .build ();
        projectRepository.save ( alreadyExistsProject );

        //when
        boolean isExistByTitle = projectRepository.existsByTitle ( projectRequest.getTitle ( ) );
        System.out.println (isExistByTitle );
        //then
        assertFalse ( isExistByTitle );
    }

    @Test
    @DisplayName ( "should return the project if it contains this root administrator")
    public void findProjectByRootAdministratorEmail() {
        //given
        String email = "ajith@gmail.com";
        Project projectOne = Project.builder ()
                .title ( "pedal" )
                .description ( "This is a sample project" )
                .projectAdministrators ( new ArrayList <> ( ) )
                .project_profile_url ( null )
                .assignedMembersIds ( new ArrayList <> (  ) )
                .projectRootAdministratorEmail ( email )
                .createdAt ( LocalDateTime.now () )
                .is_deleted ( false )
                .build ();
        Project projectTwo = Project.builder ()
                .title ( "planet" )
                .description ( "This is a sample project" )
                .projectAdministrators ( new ArrayList <> ( ) )
                .project_profile_url ( null )
                .assignedMembersIds ( new ArrayList <> (  ) )
                .projectRootAdministratorEmail ( email )
                .createdAt ( LocalDateTime.now () )
                .is_deleted ( false )
                .build ();

        projectRepository.save ( projectOne );
        projectRepository.save ( projectTwo );

        //when
        List <Project> existingProjectList = projectRepository.findByProjectRootAdministratorEmail ( email );
        for ( Project project : existingProjectList ){
            System.out.println (project.getTitle () +"----------------------------------------------------------------" +project.getId ());
        }
        //then
        assertNotNull ( existingProjectList );
        assertFalse(existingProjectList.isEmpty());
        assertEquals ( 2 , existingProjectList.size());
        assertTrue(existingProjectList.contains(projectOne));
        assertTrue(existingProjectList.contains(projectTwo));



    }
}