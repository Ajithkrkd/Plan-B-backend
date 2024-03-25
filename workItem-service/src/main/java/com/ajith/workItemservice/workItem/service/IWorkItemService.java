package com.ajith.workItemservice.workItem.service;

import com.ajith.workItemservice.utils.BasicResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

public interface IWorkItemService {
    ResponseEntity< BasicResponse> createWorkItem (String workItemCategory, String title, Optional< String> parentWorkItemId, String projectId, String authHeader);

    ResponseEntity< BasicResponse> assignMemberToWorkItem (String workItemId, Long userId);

    ResponseEntity< BasicResponse> changeWorkItemState (String workItemId, String newState);
}
