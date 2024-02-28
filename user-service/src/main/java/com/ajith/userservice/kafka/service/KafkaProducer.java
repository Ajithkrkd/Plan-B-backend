package com.ajith.userservice.kafka.service;

import com.ajith.userservice.kafka.event.UserEmailTokenEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, UserEmailTokenEvent > kafkaTemplate;

    public void sentMessage(UserEmailTokenEvent event) {
        try{
            Message <UserEmailTokenEvent> message = MessageBuilder
                    .withPayload ( event )
                    .setHeader ( KafkaHeaders.TOPIC,"email-verification-topic" )
                    .build ();
            kafkaTemplate.send ( message );
            log.info ( "email verification event produced"+ event );
        }
        catch (Exception e) {
            log.error("Error occurred while sending message to Kafka: {}", e.getMessage(), e);
            // Handle the exception as needed
        }
    }
}
