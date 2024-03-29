package com.ajith.workItemservice.workItem.entity;

import com.ajith.workItemservice.comments.entity.Comment;
import com.ajith.workItemservice.label.entity.Label;
import com.ajith.workItemservice.workItem.enums.WorkItemCategory;
import com.ajith.workItemservice.workItem.enums.WorkItemState;
import com.ajith.workItemservice.workingLifeCycle.entity.WorkingLifeCycle;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
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
    @Enumerated(EnumType.STRING)
    private WorkItemCategory category;
    private Long projectId;
    private WorkItemState state;
    private Long parentWorkItemId;
    private Date createdAt;
    private boolean isDeleted =false;
    private boolean isArchived = false;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "work_item_comment",
            joinColumns = @JoinColumn(name = "work_item_id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id")
    )

    private List<Comment> comments;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "work_item_label",
            joinColumns = @JoinColumn(name = "work_item_id"),
            inverseJoinColumns = @JoinColumn(name = "label_id")
    )
    private List<Label> labels;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "working_life_cycle_id")
    private WorkingLifeCycle workingLifeCycle;

    @ElementCollection
    private List<Long> membersAssigned;

}
