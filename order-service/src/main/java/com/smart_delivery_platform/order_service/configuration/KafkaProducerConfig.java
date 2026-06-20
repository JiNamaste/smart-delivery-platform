package com.smart_delivery_platform.order_service.configuration;

import com.smart_delivery_platform.order_service.dto.OrderCreatedEvent;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public ProducerFactory<String, OrderCreatedEvent> orderCreatedEventProducerFactory(KafkaProperties kafkaProperties) {
        return new DefaultKafkaProducerFactory<>(kafkaProperties.buildProducerProperties());
    }

    @Bean
    public KafkaTemplate<String, OrderCreatedEvent> orderCreatedEventKafkaTemplate(
            ProducerFactory<String, OrderCreatedEvent> orderCreatedEventProducerFactory) {
        return new KafkaTemplate<>(orderCreatedEventProducerFactory);
    }
}
