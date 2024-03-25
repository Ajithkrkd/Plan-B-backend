package com.ajith.workItemservice.utils;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BasicResponse {

    private int status;
    private String message;
    private String description;
    private LocalDateTime timestamp;

}
