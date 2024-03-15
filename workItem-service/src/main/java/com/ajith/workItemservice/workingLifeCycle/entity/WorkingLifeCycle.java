package com.ajith.workItemservice.workingLifeCycle.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class WorkingLifeCycle {
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long workingLifeCycleId;
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
