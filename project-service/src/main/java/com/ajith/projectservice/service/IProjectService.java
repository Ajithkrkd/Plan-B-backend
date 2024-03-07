package com.ajith.projectservice.service;

import com.ajith.projectservice.dto.ProjectRequest;
import com.ajith.projectservice.utils.BasicResponse;
import org.springframework.http.ResponseEntity;

public interface IProjectService {
    ResponseEntity< BasicResponse> createProject (ProjectRequest projectRequest, String authHeader);
}
