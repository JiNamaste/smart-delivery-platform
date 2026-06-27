package com.smart_delivery_platform.delivery_service.service;

import com.smart_delivery_platform.delivery_service.dto.DeliveryAssignedEvent;
import com.smart_delivery_platform.delivery_service.dto.RestaurantConfirmedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class DeliveryConsumer {

    private final DeliveryProducer deliveryProducer;

    @Value("${app.event-delay-ms:600}")
    private long eventDelayMs;

    public DeliveryConsumer(DeliveryProducer deliveryProducer) {
        this.deliveryProducer = deliveryProducer;
    }

    @KafkaListener(
            topics = "restaurant-confirmed",
            groupId = "delivery-group"
    )
    public void consume(RestaurantConfirmedEvent event) {
        applyEventDelay("delivery assignment", event.getOrderId());
        System.out.println("Delivery partner assigned for order: " + event.getOrderId());
        DeliveryAssignedEvent assignedEvent = new DeliveryAssignedEvent(
                        event.getOrderId(),
                        "DP-101",
                        "Rahul",
                        "ASSIGNED"
        );
        deliveryProducer.publishDeliveryAssigned(assignedEvent);
    }

    private void applyEventDelay(String stage, String orderId) {
        if (eventDelayMs <= 0) {
            return;
        }

        try {
            System.out.println("Delaying " + stage + " for order " + orderId + " by " + eventDelayMs + " ms");
            Thread.sleep(eventDelayMs);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}
