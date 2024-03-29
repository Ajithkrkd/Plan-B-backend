package com.ajith.notificationservice.event;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserEmailTokenEvent {
    private String fullName;
    private String email;
    private String token;
    private String confirmation_link;
}

