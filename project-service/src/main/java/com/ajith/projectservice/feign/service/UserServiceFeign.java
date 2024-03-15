package com.ajith.projectservice.feign.service;

import com.ajith.projectservice.feign.dto.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient("user-service")
public interface UserServiceFeign {

    @GetMapping("/user/api/secure/get_user_by_authHeader")
    public ResponseEntity< User > getUserByAuthHeader(
            @RequestHeader("Authorization") String authHeader);

    @PostMapping ("/user/api/secure/getUserByIds")
    public ResponseEntity< List< User > > getUserByIds(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody List<Long>usersIds
    );
}
