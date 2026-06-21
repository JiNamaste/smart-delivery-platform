package com.smart_delivery_platform.delivery_service.service;

import com.smart_delivery_platform.delivery_service.dto.DeliveryAssignedEvent;
import com.smart_delivery_platform.delivery_service.dto.RestaurantConfirmedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class DeliveryConsumer {

    private final DeliveryProducer deliveryProducer;

    public DeliveryConsumer(DeliveryProducer deliveryProducer) {
        this.deliveryProducer = deliveryProducer;
    }

    @KafkaListener(
            topics = "restaurant-confirmed",
            groupId = "delivery-group"
    )
    public void consume(RestaurantConfirmedEvent event) {
        System.out.println("Delivery partner assigned for order: " + event.getOrderId());
        DeliveryAssignedEvent assignedEvent = new DeliveryAssignedEvent(
                        event.getOrderId(),
                        "DP-101",
                        "Rahul",
                        "ASSIGNED"
                );
        deliveryProducer.publishDeliveryAssigned(assignedEvent);
    }
}