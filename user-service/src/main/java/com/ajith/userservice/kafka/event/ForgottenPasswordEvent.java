package com.ajith.userservice.kafka.event;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForgottenPasswordEvent {
    private String fullName;
    private String email;
    private String token;
    private String confirmation_link;
}
