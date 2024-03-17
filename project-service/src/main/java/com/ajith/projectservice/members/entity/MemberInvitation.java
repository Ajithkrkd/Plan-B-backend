package com.ajith.projectservice.members.entity;

import com.ajith.projectservice.entity.Project;
import com.ajith.projectservice.members.enums.InvitationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class MemberInvitation {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    private String memberEmail;
    private String invitationMessage;
    private String invitationToken;

    @Enumerated(EnumType.STRING)
    private InvitationStatus status;

    private Date createdAt;
    private Date expiryDate;
}



