package com.ajith.projectservice.service;

import com.ajith.projectservice.dto.ProjectDetailsWithOutMembers;
import com.ajith.projectservice.dto.ProjectRequest;
import com.ajith.projectservice.utils.BasicResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IProjectService {
    ResponseEntity< BasicResponse> createProject (ProjectRequest projectRequest, String authHeader);

    ResponseEntity< List<ProjectDetailsWithOutMembers> > getAllProjectDetails (String authHeader);
}
