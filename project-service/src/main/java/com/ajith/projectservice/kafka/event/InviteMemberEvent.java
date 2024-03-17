package com.ajith.projectservice.kafka.event;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InviteMemberEvent {
    private String email;
    private String message;
    private String token;
    private Date expirationTime;
}
