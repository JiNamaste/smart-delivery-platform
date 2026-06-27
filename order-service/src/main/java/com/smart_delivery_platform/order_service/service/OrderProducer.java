package com.smart_delivery_platform.order_service.service;

import com.smart_delivery_platform.order_service.dto.OrderCreatedEvent;
import com.smart_delivery_platform.order_service.dto.PaymentSuccessEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishOrderEvent(OrderCreatedEvent event) {
        if (event == null || event.getOrderId() == null) {
            throw new IllegalArgumentException("Order request and orderId are required");
        }
        kafkaTemplate.send("order-created", event);
        System.out.println("Order event published: " + event);
    }

    public void publishPaymentSuccess(PaymentSuccessEvent event){
        kafkaTemplate.send("order-payment-success", event).whenComplete((result, ex) -> {
            if (ex != null) {
                System.out.println("Failed to publish order-payment-success event: " + ex.getMessage());
                return;
            }
            System.out.println("Order payment success event published: " + event);
        });
    }
}
