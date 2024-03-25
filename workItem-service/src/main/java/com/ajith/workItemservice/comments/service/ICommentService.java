package com.ajith.workItemservice.comments.service;

import com.ajith.workItemservice.comments.entity.Comment;
import com.ajith.workItemservice.utils.BasicResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ICommentService {
    ResponseEntity< BasicResponse> createComment (String authHeader, String content,String workItem);

    ResponseEntity< BasicResponse> editComment (String content, String commentId);


    ResponseEntity< BasicResponse> deleteComment (String commentId);

    ResponseEntity< List< Comment>> getAllComments (String workItemId);
}
