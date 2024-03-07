package com.ajith.projectservice.utils;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BasicResponse {
    private int status;
    private String message;
    private String description;
    private LocalDateTime timestamp;
}
