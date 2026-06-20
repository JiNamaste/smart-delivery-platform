package com.smart_delivery_platform.order_service.service;

import com.smart_delivery_platform.order_service.dto.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderProducer {

    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    public void publishOrderEvent(OrderCreatedEvent event) {
        kafkaTemplate.send("order-created", event);
        System.out.println("Order event published: " + event);
    }
}