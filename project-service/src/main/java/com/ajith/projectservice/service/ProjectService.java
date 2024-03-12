package com.ajith.projectservice.service;

import com.ajith.jwtutilpackage.jwt.JwtService;
import com.ajith.projectservice.dto.ProjectDetailsWithOutMembers;
import com.ajith.projectservice.dto.ProjectRequest;
import com.ajith.projectservice.entity.Project;
import com.ajith.projectservice.exceptions.UserNotFoundException;
import com.ajith.projectservice.feign.dto.User;
import com.ajith.projectservice.feign.service.UserServiceFeign;
import com.ajith.projectservice.repository.ProjectRepository;
import com.ajith.projectservice.utils.BasicResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectService implements IProjectService{

    private final UserServiceFeign userServiceFeign;
    private final ProjectRepository projectRepository;
    @Override
    public ResponseEntity < BasicResponse > createProject (ProjectRequest projectRequest, String authHeader) {
        try{
            Optional< User > user = Optional.ofNullable ( userServiceFeign.getUserByAuthHeader ( authHeader ).getBody ( ) );
            log.info ( user + " user got from user service" );
            if( user.isPresent ( ) ){
                User validUser = user.get ();
                //TODO: custom exception
              boolean isProjectTitleDuplicating = projectRepository.existsByTitle(projectRequest.getTitle ());
                if(isProjectTitleDuplicating){
                      return ResponseEntity.status (HttpStatus.CONFLICT).body ( BasicResponse.builder()
                            .message ( "Project Title is already exist" )
                            .description ( "your project title  "+ projectRequest.getTitle ()+"  is already exist try another one   " )
                            .timestamp ( LocalDateTime.now () )
                            .status ( HttpStatus.CONFLICT.value ( ) )
                            .build());
                }
                Project project =  Project.builder ()
                        .createdAt ( LocalDateTime.now () )
                        .projectRootAdministrator ( validUser.getEmail () )
                        .projectAdministrators ( Collections.singletonList ( validUser.getUserId ( ) ) )
                        .title ( projectRequest.getTitle () )
                        .description ( projectRequest.getDescription() )
                        .is_deleted ( false )
                        .assignedMembersIds ( Collections.singletonList ( validUser.getUserId () ) )
                        .build ();
                projectRepository.save ( project );

                return ResponseEntity.ok( BasicResponse.builder()
                        .message ( "Project created successfully" )
                        .description ( "your project is created and assigned to you "+ validUser.getFullName () )
                        .timestamp ( LocalDateTime.now () )
                        .status ( HttpStatus.CREATED.value ( ) )
                        .build());
            }else{
                throw new UserNotFoundException ("user not found ");
            }
        }
        catch (UserNotFoundException e) {
            throw new RuntimeException ( e );
        }
        catch (Exception e){
            throw new RuntimeException ( e.getMessage () );
        }
    }

    @Override
    public ResponseEntity < List <ProjectDetailsWithOutMembers> > getAllProjectDetails (String authHeader) {
        String token = JwtService.getTokenFromAuthHeader ( authHeader );
        String userName = JwtService.getUsernameFromToken (token);
        try{
        List <Project> projectList = projectRepository.findByProjectRootAdministrator(userName);
        if( projectList.isEmpty ( ) ) {
            //TODO: custom error exception
            return ResponseEntity.noContent().build();
        }
        List<ProjectDetailsWithOutMembers>projectDetails = projectList.stream ()
                .map ( this::createProjectDetailsWithOutMembers )
                .toList ( );
        return ResponseEntity.ok ( projectDetails );
    }catch ( Exception e ){
        throw new RuntimeException ( e.getMessage() );

    }
    }

    private ProjectDetailsWithOutMembers createProjectDetailsWithOutMembers (Project project) {
       return ProjectDetailsWithOutMembers.builder ()
                .projectId ( project.getId ( ) )
                .project_profile_url ( project.getProject_profile_url () )
                .title ( project.getTitle() )
                .description ( project.getDescription() )
                .build ();
    }
}
