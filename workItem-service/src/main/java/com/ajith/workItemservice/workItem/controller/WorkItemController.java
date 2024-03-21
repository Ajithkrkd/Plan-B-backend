package com.ajith.workItemservice.workItem.controller;

import com.ajith.workItemservice.utils.BasicResponse;
import com.ajith.workItemservice.workItem.service.IWorkItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/work-item")
@RequiredArgsConstructor
public class WorkItemController {

    private final IWorkItemService iWorkItemService;


    @PostMapping("/create")
    public ResponseEntity< BasicResponse > createWorkItem(
            @RequestParam String title,
            @RequestParam String workItemCategory,
            @RequestParam Optional<String> parentWorkItemId,
            @RequestParam String projectId,
            @RequestHeader ("Authorization") String authHeader){

        return iWorkItemService.createWorkItem(workItemCategory,title, parentWorkItemId,projectId,authHeader);
    }

}
