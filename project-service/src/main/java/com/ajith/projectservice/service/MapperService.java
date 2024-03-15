package com.ajith.projectservice.service;

import com.ajith.projectservice.dto.ProjectDetailsResponse;
import com.ajith.projectservice.dto.ProjectDetailsWithOutMembers;
import com.ajith.projectservice.dto.ProjectRequest;
import com.ajith.projectservice.dto.user.AssignedMembersDetails;
import com.ajith.projectservice.dto.user.ProjectAdministratorDetails;
import com.ajith.projectservice.entity.Project;
import com.ajith.projectservice.exceptions.ResourceNotFoundException;
import com.ajith.projectservice.feign.dto.User;
import com.ajith.projectservice.feign.service.UserServiceFeign;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MapperService {

    private final UserServiceFeign userServiceFeign;
   public ProjectDetailsWithOutMembers createProjectDetailsWithOutMembers (Project project) {
        return ProjectDetailsWithOutMembers.builder ()
                .projectId ( project.getId ( ) )
                .project_profile_url ( project.getProject_profile_url () )
                .title ( project.getTitle() )
                .description ( project.getDescription() )
                .build ();
    }

    public Project mapProjectRequestToProject (ProjectRequest projectRequest, User validUser) {
        Project project =  Project.builder ()
                .createdAt ( LocalDateTime.now () )
                .projectRootAdministratorEmail ( validUser.getEmail () )
                .projectAdministrators ( Collections.singletonList ( validUser.getUserId ( ) ) )
                .title ( projectRequest.getTitle () )
                .description ( projectRequest.getDescription() )
                .is_deleted ( false )
                .assignedMembersIds ( Collections.singletonList ( validUser.getUserId () ) )
                .build ();
        return project;
    }

    public ProjectDetailsResponse mapProjectToProjectDetailsResponse (Project existingProject ,String authHeader) {
    try{
        List<ProjectAdministratorDetails> projectAdministratorDetails
                    = getProjectAdministratorsList ( existingProject, authHeader );
        List< AssignedMembersDetails > assignedMembersDetails
                = getAssignedMemebersList ( existingProject, authHeader );

        return   ProjectDetailsResponse.builder ( )
                .projectId ( existingProject.getId () )
                .title ( existingProject.getTitle () )
                .description ( existingProject.getDescription () )
                .project_profile_url ( existingProject.getProject_profile_url ( ) )
                .createdDateTime (existingProject.getCreatedAt () )
                .assignedMembersDetailsList ( assignedMembersDetails )
                .projectAdministratorDetailsList (projectAdministratorDetails  )
                .projectRootAdministratorEmail ( existingProject.getProjectRootAdministratorEmail () )
                .build ( );
    }catch (Exception e){
        throw new RuntimeException(e.getMessage());
    }


    }

    private List< AssignedMembersDetails> getAssignedMemebersList (Project existingProject, String authHeader) {
        List < User > usersList = userServiceFeign.getUserByIds ( authHeader, existingProject.getAssignedMembersIds () ).getBody ();
        if(usersList.isEmpty ()){
            throw new ResourceNotFoundException ("Your trying to get users list by Assigned members ids but it is empty");
        }
        return usersList.stream ()
                .map ( user -> new AssignedMembersDetails (
                        user.getUserId () ,
                        user.getFullName ( ),
                        user.getEmail ( ),
                        user.getProfile_image_path ( )!=null ? user.getProfile_image_path ( ) : null  ))
                .toList ();
    }


    private List<ProjectAdministratorDetails> getProjectAdministratorsList (Project existingProject, String authHeader) {
        List < User > usersList = userServiceFeign.getUserByIds ( authHeader, existingProject.getProjectAdministrators () ).getBody ();
        if(usersList.isEmpty ()){
            throw new ResourceNotFoundException ("Your trying to get users list by project administrators ids but it is empty");
        }
        return usersList.stream ()
                .map ( user -> new ProjectAdministratorDetails (
                                user.getUserId () ,
                                user.getFullName ( ),
                                user.getEmail ( ),
                                user.getProfile_image_path ( )!=null ? user.getProfile_image_path ( ) : null  ))
                .toList ();
    }
}
