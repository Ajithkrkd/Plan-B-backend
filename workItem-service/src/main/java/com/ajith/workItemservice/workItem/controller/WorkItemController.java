package com.ajith.workItemservice.workItem.controller;

import com.ajith.workItemservice.utils.BasicResponse;
import com.ajith.workItemservice.workItem.service.IWorkItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/workItem")
@RequiredArgsConstructor
public class WorkItemController {

    private final IWorkItemService iWorkItemService;

    //Todo if the work item epic only add child as issue can't add child task to epic
    @PostMapping("/create")
    public ResponseEntity< BasicResponse > createWorkItem(
            @RequestParam String title,
            @RequestParam String workItemCategory,
            @RequestParam Optional<String> parentWorkItemId,
            @RequestParam String projectId,
            @RequestHeader ("Authorization") String authHeader){

        return iWorkItemService.createWorkItem(workItemCategory,title, parentWorkItemId,projectId,authHeader);
    }

    @PostMapping("/changeState")
    public ResponseEntity<BasicResponse> changeWorkItemState(
            @RequestParam ("newState") String newState,
            @RequestParam("workItemId") String workItemId){
        return iWorkItemService.changeWorkItemState(workItemId,newState);
    }

}
