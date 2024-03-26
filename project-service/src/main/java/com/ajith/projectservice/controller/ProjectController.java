package com.ajith.projectservice.controller;

import com.ajith.projectservice.dto.ProjectDetailsResponse;
import com.ajith.projectservice.dto.ProjectDetailsWithOutMembers;
import com.ajith.projectservice.dto.ProjectRequest;
import com.ajith.projectservice.exceptions.ResourceAlreadyExist;
import com.ajith.projectservice.exceptions.UserNotFoundException;
import com.ajith.projectservice.service.IProjectService;
import com.ajith.projectservice.utils.BasicResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {

    private final IProjectService iProjectService;
    @PostMapping("/create")
    @ResponseStatus( HttpStatus.CREATED)
    public ResponseEntity< BasicResponse > createProject(
            @RequestBody ProjectRequest projectRequest,
            @RequestHeader ("Authorization") String authHeader) throws UserNotFoundException, ResourceAlreadyExist {
     return    iProjectService.createProject(projectRequest ,authHeader);
    }

    @GetMapping("/get_all_projects") // all projects of user getting by token
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity< List<ProjectDetailsWithOutMembers> > getAllProjectDetails(
            @RequestHeader ("Authorization") String authHeader){
        return iProjectService.getAllProjectDetails(authHeader);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity< ProjectDetailsResponse > getProjectByProjectId(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable("projectId") String projectId
    ){
        Long project_ID = Long.valueOf ( projectId );
        return iProjectService.getProjectByProjectId(authHeader,project_ID);
    }


    @GetMapping("/checkProjectIsExist")
    public boolean isProjectExist(@RequestParam Long projectId){
        return iProjectService.isProjectExist(projectId);
    }
}
