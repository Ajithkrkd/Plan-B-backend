package com.ajith.workItemservice.feign.project;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("project-service")
public interface ProjectFeignService {

    @GetMapping("/project/checkProjectIsExist")
    boolean isProjectExist(@RequestParam Long projectId);
}
