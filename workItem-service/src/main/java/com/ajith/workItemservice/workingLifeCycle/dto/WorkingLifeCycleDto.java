package com.ajith.workItemservice.workingLifeCycle.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class WorkingLifeCycleDto {

    private String title;
    private Date startTime;
    private Date endTime;

}
