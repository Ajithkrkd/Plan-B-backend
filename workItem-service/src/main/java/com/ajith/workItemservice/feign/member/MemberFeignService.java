package com.ajith.workItemservice.feign.member;

import com.ajith.workItemservice.members.dto.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient("user-service")
public interface MemberFeignService {


    @GetMapping ("/user/api/secure/getUserById")
     ResponseEntity<User> getMemberById(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam ("memberId") String memberId
    );
    @GetMapping("/user/api/secure/get_user_by_authHeader")
    ResponseEntity< User > getUserByAuthHeader(
            @RequestHeader("Authorization") String authHeader);

}
