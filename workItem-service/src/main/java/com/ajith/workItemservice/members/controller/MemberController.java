package com.ajith.workItemservice.members.controller;

import com.ajith.workItemservice.exceptions.UserNotFoundException;
import com.ajith.workItemservice.feign.member.MemberFeignService;
import com.ajith.workItemservice.members.dto.User;
import com.ajith.workItemservice.utils.BasicResponse;
import com.ajith.workItemservice.workItem.service.IWorkItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/workItem/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberFeignService memberFeignService;
    private final IWorkItemService iWorkItemService;
    @PostMapping ("/assignToWorkItem")
    public ResponseEntity< BasicResponse > assignToMemberToWorkItem(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam ("workItemId") String workItemId,
            @RequestParam String memberId
    )
    {
        try{
            User expectedUser = memberFeignService.getMemberById ( authHeader,memberId ).getBody ();
            return iWorkItemService.assignMemberToWorkItem (workItemId ,expectedUser.getUserId ());
        } catch (UserNotFoundException e) {
            throw new RuntimeException ( e );
        }
    }
}
