package com.ajith.workItemservice.workingLifeCycle.controller;

import com.ajith.workItemservice.utils.BasicResponse;
import com.ajith.workItemservice.workingLifeCycle.dto.WorkingLifeCycleDto;
import com.ajith.workItemservice.workingLifeCycle.entity.WorkingLifeCycle;
import com.ajith.workItemservice.workingLifeCycle.service.IWorkingLifeCycleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workItem/workingLifeCycle")
@RequiredArgsConstructor
public class WorkingLifeCycleController {

    private final IWorkingLifeCycleService iWorkingLifeCycleService;

    @GetMapping("/getAll")
    public ResponseEntity< List < WorkingLifeCycle > > getAllWorkingLifeCycles(
            @RequestHeader("Authorization") String authHeader
    ) {
        return iWorkingLifeCycleService.getAllWorkingLifeCycles(authHeader);
    }

    @PostMapping("/create")
    public ResponseEntity< BasicResponse > createWorkingLifeCycle(
            @RequestHeader ("Authorization") String authHeader,
            @RequestBody WorkingLifeCycleDto workingLifeCycleDto

    ){
        return iWorkingLifeCycleService.createWorkingLifeCycle(authHeader,workingLifeCycleDto);
    }

    @PostMapping("/edit/{workingLifeCycleId}")
    public ResponseEntity< BasicResponse > editWorkingLifeCycle(
            @PathVariable("workingLifeCycleId") String workingLifeCycleId,
            @RequestBody WorkingLifeCycleDto workingLifeCycleDto
            ){
        return iWorkingLifeCycleService.editWorkingLifeCycle(workingLifeCycleId, workingLifeCycleDto);
    }
    @PostMapping("/delete/{workingLifeCycleId}")
    public ResponseEntity< BasicResponse > deleteWorkingLifeCycle(
            @PathVariable("workingLifeCycleId") String workingLifeCycleId
    ){
        return iWorkingLifeCycleService.deleteWorkingLifeCycle(workingLifeCycleId);
    }
}
