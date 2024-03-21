package com.ajith.projectservice.service;

import com.ajith.projectservice.dto.ProjectDetailsResponse;
import com.ajith.projectservice.dto.ProjectDetailsWithOutMembers;
import com.ajith.projectservice.dto.ProjectRequest;
import com.ajith.projectservice.exceptions.ResourceAlreadyExist;
import com.ajith.projectservice.exceptions.UserNotFoundException;
import com.ajith.projectservice.members.dto.InviteRequest;
import com.ajith.projectservice.utils.BasicResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IProjectService {
    ResponseEntity < BasicResponse > createProject (ProjectRequest projectRequest, String authHeader) throws ResourceAlreadyExist, UserNotFoundException;

    ResponseEntity < List < ProjectDetailsWithOutMembers > > getAllProjectDetails (String authHeader);

    ResponseEntity < ProjectDetailsResponse > getProjectByProjectId (String authHeader, Long projectId);

    boolean isProjectExist (Long projectId);
}
