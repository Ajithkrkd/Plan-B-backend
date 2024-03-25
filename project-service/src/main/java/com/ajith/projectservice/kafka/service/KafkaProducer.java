package com.ajith.projectservice.kafka.service;

import com.ajith.projectservice.kafka.event.InviteMemberEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {


    private final KafkaTemplate <String, InviteMemberEvent > kafkaTemplate;

    public void sentMessage(InviteMemberEvent event) {
        try{
            Message <InviteMemberEvent> message = MessageBuilder
                    .withPayload ( event )
                    .setHeader ( KafkaHeaders.TOPIC,"invite-member-topic" )
                    .build ();
            kafkaTemplate.send ( message );
            log.info ( "invite member event created "+ event );
        }
        catch (Exception e) {
            log.error("Error occurred while sending message to Kafka: {}", e.getMessage(), e);
            // Handle the exception as needed
        }
    }

}
