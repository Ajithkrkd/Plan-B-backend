package com.ajith.workItemservice.label.controller;

import com.ajith.workItemservice.exceptions.UserNotFoundException;
import com.ajith.workItemservice.feign.member.MemberFeignService;
import com.ajith.workItemservice.label.service.ILabelService;
import com.ajith.workItemservice.members.dto.User;
import com.ajith.workItemservice.utils.BasicResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/workItem/label")
@RequiredArgsConstructor
public class LabelController {

    private final ILabelService ilabelService;
    private final MemberFeignService memberFeignService;

    @PostMapping("/create")
    public ResponseEntity< BasicResponse > createAndAddLabelToWorkItem(
            @RequestHeader ("Authorization") String authHeader,
            @RequestParam ("workItemId") String workItemId,
            @RequestParam ("label") String labelTitle
    ){
        try {
            User expectedUser = memberFeignService.getUserByAuthHeader (authHeader).getBody ();
            if (expectedUser == null){
                throw new UserNotFoundException ( "user not fount with this authHeader" );
            }
            return ilabelService.createAndAddLabelToWorkItem(workItemId, labelTitle,expectedUser);
        } catch (UserNotFoundException e) {
            throw e;
        }

    }

    @PostMapping("/delete")
    public ResponseEntity<BasicResponse> deleteLabel(
            @RequestParam ("labelId") String labelId,
            @RequestParam ("workItemId") String workItemId){
            return ilabelService.deleteLabel(labelId,workItemId);
    }

    @GetMapping("/all/{workItemId}")
    public ResponseEntity< List <String> >getAllLabelsByWorkItemId(
            @PathVariable ("workItemId") String workItemId){
        return ilabelService.getAllLabelsByWorkItemId(workItemId);
    }
}
