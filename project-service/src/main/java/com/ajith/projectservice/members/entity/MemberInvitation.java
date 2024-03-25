package com.ajith.projectservice.members.entity;

import com.ajith.projectservice.entity.Project;
import com.ajith.projectservice.members.enums.InvitationStatus;
import jakarta.persistence.*;
import lombok.*;

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

    private String inviteSentTo;
    private String invitationMessage;
    private String invitationToken;
    private String inviteFrom;
    @Enumerated(EnumType.STRING)
    private InvitationStatus status;

    private Date createdAt;
    private Date expiryDate;
}



