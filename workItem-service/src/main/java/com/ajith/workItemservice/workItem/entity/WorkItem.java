package com.ajith.workItemservice.workItem.entity;

import com.ajith.workItemservice.comments.entity.Comment;
import com.ajith.workItemservice.workingLifeCycle.entity.WorkingLifeCycle;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WorkItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long workItemId;
    private String title;
    private String description;
    private Long projectId;
    private Long parentWorkItemId;
    private List< Comment >comments;
    private WorkingLifeCycle workingLifeCycle;
    @ElementCollection
    private List<Long> membersAssigned;

}
