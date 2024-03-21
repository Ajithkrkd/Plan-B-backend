package com.ajith.workItemservice.workItem.entity;

import com.ajith.workItemservice.comments.entity.Comment;
import com.ajith.workItemservice.workItem.enums.WorkItemCategory;
import com.ajith.workItemservice.workingLifeCycle.entity.WorkingLifeCycle;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class WorkItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long workItemId;
    private String title;
    private String description;
    private WorkItemCategory category;
    private Long projectId;
    private Long parentWorkItemId;
    private List< Comment >comments;
    private WorkingLifeCycle workingLifeCycle;
    @ElementCollection
    private List<Long> membersAssigned;

}
