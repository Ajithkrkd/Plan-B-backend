package com.ajith.userservice.kafka.event;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEmailTokenEvent {
    private String fullName;
    private String email;
    private String token;
}
