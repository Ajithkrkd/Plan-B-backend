package com.ajith.workItemservice.label.entity;

import com.ajith.workItemservice.workItem.entity.WorkItem;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Label {
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long labelId;
    private String labelName;
    private Long createdBy;
}
