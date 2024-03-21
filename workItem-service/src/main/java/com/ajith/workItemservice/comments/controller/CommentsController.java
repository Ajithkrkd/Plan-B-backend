package com.ajith.workItemservice.comments.controller;

import com.ajith.workItemservice.comments.service.ICommentService;
import com.ajith.workItemservice.utils.BasicResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/workItem/comment")
@RequiredArgsConstructor
public class CommentsController {

    private final ICommentService iCommentService;

    @PostMapping("/create")
    public ResponseEntity< BasicResponse > createComment(
            @RequestHeader ("Authorization") String authHeader,
            @RequestParam ("workItemId") String workItem,
            @RequestBody String content){
        return iCommentService.createComment(authHeader, content,workItem);
    }
}
