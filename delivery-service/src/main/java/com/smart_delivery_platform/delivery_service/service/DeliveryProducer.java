package com.smart_delivery_platform.delivery_service.service;

import com.smart_delivery_platform.delivery_service.dto.DeliveryAssignedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryProducer {

    private final KafkaTemplate<String, DeliveryAssignedEvent> kafkaTemplate;

    public void publishDeliveryAssigned(DeliveryAssignedEvent event) {
        kafkaTemplate.send("delivery-assigned", event);
        System.out.println("Delivery assigned event published: " + event);
    }
}
