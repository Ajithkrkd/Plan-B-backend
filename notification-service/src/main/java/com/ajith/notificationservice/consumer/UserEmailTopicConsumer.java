package com.ajith.notificationservice.consumer;

import com.ajith.notificationservice.consumer.service.UserEmailVerificationService;
import com.ajith.notificationservice.event.UserEmailTokenEvent;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
@Slf4j
@Service
@RequiredArgsConstructor
public class UserEmailTopicConsumer {
    private final UserEmailVerificationService service;
    @KafkaListener (topics = "email-verification-topic", groupId = "user-email-topic-group")
    public void consumeJsonMsg(UserEmailTokenEvent message) throws MessagingException, UnsupportedEncodingException {
        service.sendMail ( message );
        log.info("Consuming the message from notificationTopic: {}", message.toString());
    }
}
