package com.ajith.workItemservice.label.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Label {
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long labelId;
    private String labelName;
    private Long workItemId;
    private Long createdBy;
}
