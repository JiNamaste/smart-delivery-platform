package com.smart_delivery_platform.payment_service.service;

import com.smart_delivery_platform.payment_service.dto.OrderCreatedEvent;
import com.smart_delivery_platform.payment_service.dto.PaymentSuccessEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class PaymentConsumer {
    private final KafkaTemplate<String, PaymentSuccessEvent> kafkaTemplate;

    public PaymentConsumer(KafkaTemplate<String,PaymentSuccessEvent > kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "order-created", groupId = "payment-group")
    public void consume(OrderCreatedEvent orderevent) {
        System.out.println("Payment Service Received Order: " + orderevent);
        System.out.println("Processing Payment...");

        PaymentSuccessEvent event =
                new PaymentSuccessEvent(
                        orderevent.getOrderId(),
                        "PAY-101",
                        orderevent.getAmount(),
                        "SUCCESS"
                );
        kafkaTemplate.send("payment-success", event);
        System.out.println("Payment event published: " + event);
    }



}
