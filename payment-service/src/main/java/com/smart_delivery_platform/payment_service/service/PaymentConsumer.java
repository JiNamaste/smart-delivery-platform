package com.smart_delivery_platform.payment_service.service;

import com.smart_delivery_platform.payment_service.dto.OrderCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentConsumer {

    @KafkaListener(topics = "order-created", groupId = "payment-group")
    public void consume(OrderCreatedEvent event) {
        System.out.println("Payment Service Received Order: " + event);
        System.out.println("Processing Payment...");
    }
}
