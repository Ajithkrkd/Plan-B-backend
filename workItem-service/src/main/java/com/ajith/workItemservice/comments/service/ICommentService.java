package com.ajith.workItemservice.comments.service;

import com.ajith.workItemservice.utils.BasicResponse;
import org.springframework.http.ResponseEntity;

public interface ICommentService {
    ResponseEntity< BasicResponse> createComment (String authHeader, String content,String workItem);
}
