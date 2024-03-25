package com.ajith.workItemservice.comments.controller;

import com.ajith.workItemservice.comments.entity.Comment;
import com.ajith.workItemservice.comments.service.ICommentService;
import com.ajith.workItemservice.utils.BasicResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/edit/{commentId}")
    public ResponseEntity< BasicResponse > editComment(
            @RequestBody String content,
            @PathVariable("commentId") String commentId
    ){
        return iCommentService.editComment(content, commentId);
    }

    @PostMapping("/delete/{commentId}")
    public ResponseEntity< BasicResponse > deleteComment(
            @PathVariable("commentId") String commentId
    ){
       return iCommentService.deleteComment(commentId);
    }

    @GetMapping("/getAll/{workItemId}")
    public ResponseEntity< List< Comment > > getAll(@PathVariable ("workItemId") String workItemId){
        return iCommentService.getAllComments(workItemId);
    }

}
