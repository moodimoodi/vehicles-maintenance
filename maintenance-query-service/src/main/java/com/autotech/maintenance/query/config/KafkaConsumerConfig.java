package com.autotech.maintenance.query.config;

import com.autotech.maintenance.query.messaging.DomainEventEnvelope;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;


public class KafkaConsumerConfig {


    public ConcurrentKafkaListenerContainerFactory<String, DomainEventEnvelope> maintenanceKafkaListenerContainerFactory(
            ConsumerFactory<String, DomainEventEnvelope> consumerFactory) {

        ConcurrentKafkaListenerContainerFactory<String, DomainEventEnvelope> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setConcurrency(3);
        return factory;
    }
}

