package com.ajith.workItemservice.workingLifeCycle.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class WorkingLifeCycle {
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long workingLifeCycleId;
    private String title;
    private Date startTime;
    private Date endTime;
    private boolean isDeleted=false;
    private Long createdBy;
}
