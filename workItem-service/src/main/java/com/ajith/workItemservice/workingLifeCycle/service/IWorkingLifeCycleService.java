package com.ajith.workItemservice.workingLifeCycle.service;

import com.ajith.workItemservice.utils.BasicResponse;
import com.ajith.workItemservice.workingLifeCycle.dto.WorkingLifeCycleDto;
import com.ajith.workItemservice.workingLifeCycle.entity.WorkingLifeCycle;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IWorkingLifeCycleService {
    ResponseEntity< List< WorkingLifeCycle>> getAllWorkingLifeCycles (String authHeader);

    ResponseEntity< BasicResponse> createWorkingLifeCycle (String authHeader, WorkingLifeCycleDto workingLifeCycleDto);

    ResponseEntity< BasicResponse> editWorkingLifeCycle (String workingLifeCycleId, WorkingLifeCycleDto workingLifeCycleDto);

    ResponseEntity< BasicResponse> deleteWorkingLifeCycle (String workingLifeCycleId);
}
