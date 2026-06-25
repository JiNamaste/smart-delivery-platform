package com.smart_delivery_platform.payment_service.service;

import com.smart_delivery_platform.payment_service.dto.OrderCreatedEvent;
import com.smart_delivery_platform.payment_service.dto.PaymentFailedEvent;
import com.smart_delivery_platform.payment_service.dto.PaymentSuccessEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class PaymentConsumer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public PaymentConsumer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "order-created", groupId = "payment-group")
    public void consume(OrderCreatedEvent orderevent) {
        System.out.println("Payment Service Received Order: " + orderevent);
        System.out.println("Processing Payment...");

        Random random = new Random();
        boolean paymentSuccess = random.nextBoolean();

        if(paymentSuccess){

            PaymentSuccessEvent successEvent = new PaymentSuccessEvent(
                    orderevent.getOrderId(),
                            "PAY-101",
                    orderevent.getAmount(),
                            "SUCCESS"
                    );

            kafkaTemplate.send("payment-success", successEvent);

        } else {
            PaymentFailedEvent failedEvent = new PaymentFailedEvent(
                            orderevent.getOrderId(),
                            "Insufficient Balance",
                            "FAILED"
                    );

            kafkaTemplate.send("payment-failed", failedEvent);
        }
    }



}
