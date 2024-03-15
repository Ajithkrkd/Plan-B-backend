package com.ajith.projectservice.service;

import com.ajith.jwtutilpackage.jwt.JwtService;
import com.ajith.projectservice.dto.ProjectDetailsResponse;
import com.ajith.projectservice.dto.ProjectDetailsWithOutMembers;
import com.ajith.projectservice.dto.ProjectRequest;
import com.ajith.projectservice.entity.Project;
import com.ajith.projectservice.exceptions.ProjectNotFoundException;
import com.ajith.projectservice.exceptions.ResourceAlreadyExist;
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
    private final MapperService mapperService;
    @Override
    public ResponseEntity < BasicResponse > createProject (ProjectRequest projectRequest, String authHeader)
            throws ResourceAlreadyExist, UserNotFoundException {
        try{
            Optional< User > user = Optional.ofNullable ( userServiceFeign.getUserByAuthHeader ( authHeader ).getBody ( ) );

            if(!user.isPresent ( ) ){
                throw new UserNotFoundException ("user not found ");
            }else{
                User validUser = user.get ();
              boolean isProjectTitleDuplicating = projectRepository.existsByTitle(projectRequest.getTitle ());

              if (isProjectTitleDuplicating){
                  throw new ResourceAlreadyExist ("project already exist with this title : " + projectRequest.getTitle ());
              }
                Project project = mapperService.mapProjectRequestToProject ( projectRequest, validUser );
                projectRepository.save ( project );
                return ResponseEntity.ok( BasicResponse.builder()
                        .message ( "Project created successfully" )
                        .description ( "your project is created and assigned to you "+ validUser.getFullName () )
                        .timestamp ( LocalDateTime.now () )
                        .status ( HttpStatus.CREATED.value ( ) )
                        .build());
            }
        }
        catch (ResourceAlreadyExist e){
            throw e;
        }
        catch (UserNotFoundException e) {
            throw e;
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
                .map ( project -> mapperService.createProjectDetailsWithOutMembers(project) )
                .toList ( );
        return ResponseEntity.ok ( projectDetails );
    }catch ( Exception e ){
        throw new RuntimeException ( e.getMessage() );

    }
    }

    @Override
    public ResponseEntity < ProjectDetailsResponse > getProjectByProjectId (String authHeader, Long projectId) {
      try{
          Optional<Project> expectedProject = projectRepository.findById ( projectId );
          if( !expectedProject.isPresent()){
              throw new ProjectNotFoundException ( "Project does not exist with id " + projectId);
          }
          Project existingProject = expectedProject.get ();
          ProjectDetailsResponse projectDetailsResponse = mapperService.mapProjectToProjectDetailsResponse(existingProject ,authHeader);
          return ResponseEntity.ok ( projectDetailsResponse);

      }
      catch (ProjectNotFoundException e){
          throw e;
      }
      catch ( Exception e ){
          throw new RuntimeException (e.getMessage ());
      }
    }
}
