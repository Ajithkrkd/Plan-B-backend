package com.ajith.notificationservice.event;

import lombok.*;

import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class InviteMemberEvent {
    private String email;
    private String message;
    private String token;
    private Date expirationTime;
}
