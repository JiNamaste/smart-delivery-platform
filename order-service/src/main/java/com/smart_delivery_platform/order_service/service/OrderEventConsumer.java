package com.smart_delivery_platform.order_service.service;

import com.smart_delivery_platform.order_service.dto.DeliveryAssignedEvent;
import com.smart_delivery_platform.order_service.dto.PaymentFailedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventConsumer {

    private final OrderService orderService;

    public OrderEventConsumer(OrderService orderService) {
        this.orderService = orderService;
    }


    @KafkaListener(topics = "delivery-assigned", groupId = "order-group")
    public void consume(DeliveryAssignedEvent event) {
        orderService.updateOrderStatus(event.getOrderId(), event.getStatus());
        System.out.println("Order completed for orderId: " + event.getOrderId() +
                " and delivery partner is " + event.getDeliveryPartnerName());
    }
    @KafkaListener(
            topics = "payment-failed",
            groupId = "order-group",
            properties = "spring.json.value.default.type=com.smart_delivery_platform.order_service.dto.PaymentFailedEvent"
    )
    public void paymentFailed(PaymentFailedEvent event) {
        orderService.updateOrderStatus(event.getOrderId(), event.getStatus());
        System.out.println("Order cancelled due to payment failure: " + event.getOrderId());
    }
}
