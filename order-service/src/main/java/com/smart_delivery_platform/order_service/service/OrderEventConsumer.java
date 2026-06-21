package com.smart_delivery_platform.order_service.service;

import com.smart_delivery_platform.order_service.dto.DeliveryAssignedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventConsumer {

    @KafkaListener(topics = "delivery-assigned", groupId = "order-group")
    public void consume(DeliveryAssignedEvent event) {
        System.out.println("Order completed for orderId: " + event.getOrderId() +
                " and delivery partner is " + event.getDeliveryPartnerName());
    }
}
