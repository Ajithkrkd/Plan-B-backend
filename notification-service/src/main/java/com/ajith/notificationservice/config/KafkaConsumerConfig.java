package com.ajith.notificationservice.config;

import com.ajith.notificationservice.event.InviteMemberEvent;
import com.ajith.notificationservice.event.UserEmailTokenEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Value ( ("${spring.kafka.consumer.bootstrap-servers}") )
    private String bootstrapServers;
    @Bean
    public Map <String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap <> ();
        props.put( ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        return props;
    }
    @Bean
    public ConsumerFactory <String, UserEmailTokenEvent > UserEmailAndPasswordconsumerFactory() {
        return new DefaultKafkaConsumerFactory <> (
                consumerConfigs(),
                new StringDeserializer(),
                new ErrorHandlingDeserializer<>(new JsonDeserializer <> (UserEmailTokenEvent.class, false)));
    }
    @Bean
    public ConcurrentKafkaListenerContainerFactory <String, UserEmailTokenEvent> userEmailAndPasswordKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, UserEmailTokenEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(UserEmailAndPasswordconsumerFactory());
        return factory;
    }
    @Bean
    public ConsumerFactory<String, InviteMemberEvent> InviteMemberconsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                consumerConfigs(),
                new StringDeserializer(),
                new ErrorHandlingDeserializer<>(new JsonDeserializer<>(InviteMemberEvent.class, false))); // Use InviteMemberEvent deserializer here
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, InviteMemberEvent> inviteMemberKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, InviteMemberEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(InviteMemberconsumerFactory ());
        return factory;
    }



}
