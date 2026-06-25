package com.smart_delivery_platform.order_service.service;

import com.smart_delivery_platform.order_service.dto.DeliveryAssignedEvent;
import com.smart_delivery_platform.order_service.dto.PaymentFailedEvent;
import com.smart_delivery_platform.order_service.dto.PaymentRefundedEvent;
import com.smart_delivery_platform.order_service.dto.RestaurantRejectedEvent;
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
    }

    @KafkaListener(topics ="restaurant-rejection" , groupId = "order-group",
            properties = "spring.json.value.default.type=com.smart_delivery_platform.order_service.dto.RestaurantRejectedEvent")
    public void orderRejection(RestaurantRejectedEvent event){
        orderService.updateOrderStatus(event.getOrderId(), event.getStatus());
    }

    @KafkaListener(topics = "payment-refund-success" , groupId = "order-group",
            properties = "spring.json.value.default.type=com.smart_delivery_platform.order_service.dto.RestaurantRejectedEvent")
    public void paymentRefundSuccess(PaymentRefundedEvent event){
        orderService.updateOrderStatus(event.getOrderId(), event.getStatus());
    }

    @KafkaListener(topics = "payment-refund-success" , groupId = "order-group",
            properties = "spring.json.value.default.type=com.smart_delivery_platform.order_service.dto.RestaurantRejectedEvent")
    public void paymentRefundFailure(PaymentRefundedEvent event){
        orderService.updateOrderStatus(event.getOrderId(), event.getStatus());
    }
}
