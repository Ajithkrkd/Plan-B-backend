package com.ajith.notificationservice.consumer;

import com.ajith.notificationservice.consumer.service.ForgotPasswordService;
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
public class kafkaConsumers {
    private final UserEmailVerificationService userEmailVerificationService;
    private final ForgotPasswordService forgotPasswordService;
    @KafkaListener (topics = "email-verification-topic", groupId = "user-verification-group")
    public void consumeJsonMsg(UserEmailTokenEvent message) throws MessagingException, UnsupportedEncodingException {
        userEmailVerificationService.sendMail ( message );
        log.info("Consuming the message from notificationTopic: {}", message.toString());
    }
    @KafkaListener (topics = "forgotten-password-topic", groupId = "user-verification-group")
    public void consumeForgottenPasswordMessage(UserEmailTokenEvent message) throws MessagingException, UnsupportedEncodingException {
        forgotPasswordService.sendMail ( message );
        log.info("Consuming the message from notificationTopic: {}", message.toString());
    }
}
