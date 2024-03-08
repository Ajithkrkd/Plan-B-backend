package com.ajith.projectservice.controller;

import com.ajith.projectservice.dto.ProjectDetailsWithOutMembers;
import com.ajith.projectservice.dto.ProjectRequest;
import com.ajith.projectservice.service.IProjectService;
import com.ajith.projectservice.utils.BasicResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {

    private final IProjectService iProjectService;
    @PostMapping("/create")
    public ResponseEntity< BasicResponse > createProject(
            @RequestBody ProjectRequest projectRequest,
            @RequestHeader ("Authorization") String authHeader){
     return    iProjectService.createProject(projectRequest ,authHeader);
    }

    @GetMapping("/get_all_projects")
    public ResponseEntity< ProjectDetailsWithOutMembers > getAllProjectDetails(
            @RequestHeader ("Authorization") String authHeader){
        return iProjectService.getAllProjectDetails(authHeader);
    }


}
