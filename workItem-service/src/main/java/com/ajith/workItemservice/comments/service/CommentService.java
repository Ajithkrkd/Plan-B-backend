package com.ajith.workItemservice.comments.service;

import com.ajith.workItemservice.comments.entity.Comment;
import com.ajith.workItemservice.comments.repository.CommentRepository;
import com.ajith.workItemservice.exceptions.ResourceNotFountException;
import com.ajith.workItemservice.exceptions.UserNotFoundException;
import com.ajith.workItemservice.feign.member.MemberFeignService;
import com.ajith.workItemservice.members.dto.User;
import com.ajith.workItemservice.utils.BasicResponse;
import com.ajith.workItemservice.workItem.entity.WorkItem;
import com.ajith.workItemservice.workItem.repository.WorkItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService implements ICommentService {

    private final CommentRepository commentRepository;
    private final MemberFeignService memberFeignService;
    private final WorkItemRepository workItemRepository;
    @Override
    public ResponseEntity < BasicResponse > createComment (String authHeader, String content,String workItemId) {
        try {
            User optionalUser = memberFeignService.getUserByAuthHeader ( authHeader ).getBody ();
            if(optionalUser == null) {
                throw new UserNotFoundException ( "User with" + authHeader + "does not exist" );
            }
            Long workId = Long.valueOf(workItemId);
            Optional < WorkItem > optionalWorkItem = workItemRepository.findById(workId);
            if (!optionalWorkItem.isPresent()) {
                throw new ResourceNotFountException ("Work item does not exist with this work item id " + workItemId);
            }
            WorkItem existingWorkItem = optionalWorkItem.get();

          Comment savedComment =  createAndSaveLabel ( content, optionalUser,workId );
            ResponseEntity.ok ( BasicResponse.builder ()
                    .message ( "Comment created successfully" )
                    .description ( "comment added to the workItem "+ workItemId ));
        } catch (UserNotFoundException e) {
            throw new RuntimeException ( e );
        }catch (ResourceNotFountException e){
            throw e;
        }
    }

    private Comment createAndSaveLabel (String content, User optionalUser,Long workItemId) {
        Comment newComment = Comment.builder ( )
                .commentedBy ( optionalUser.getUserId () )
                .commentedOn ( LocalDateTime.now () )
                .isDeleted ( false )
                .workItemId ( workItemId )
                .content ( content )
                .build ( );

        return commentRepository.save(newComment);
    }
}
