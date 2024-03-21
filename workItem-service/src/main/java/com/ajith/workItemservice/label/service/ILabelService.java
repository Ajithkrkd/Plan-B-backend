package com.ajith.workItemservice.label.service;

import com.ajith.workItemservice.members.dto.User;
import com.ajith.workItemservice.utils.BasicResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ILabelService {
    ResponseEntity< BasicResponse> createAndAddLabelToWorkItem (String workItemId, String labelTitle, User expectedUser);

    ResponseEntity< BasicResponse> deleteLabel (String labelId,String workItemId);

    ResponseEntity< List< String>> getAllLabelsByWorkItemId (String workItemId);
}
