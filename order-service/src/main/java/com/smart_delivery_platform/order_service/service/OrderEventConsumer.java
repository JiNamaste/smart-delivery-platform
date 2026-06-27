package com.smart_delivery_platform.order_service.service;

import com.smart_delivery_platform.order_service.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final IOrderService orderService;
    private final OrderProducer orderProducer;

    @KafkaListener(
            topics = "payment-success",
            groupId = "order-group",
            properties = "spring.json.value.default.type=com.smart_delivery_platform.order_service.dto.PaymentSuccessEvent"
    )
    public void paymentSuccess(PaymentSuccessEvent event) {
        boolean updated = updateOrderStatus(event.getOrderId(), event.getStatus(), "payment-success");
        if (updated) {
            orderProducer.publishPaymentSuccess(event);
        }
    }

    @KafkaListener(
            topics = "payment-failed",
            groupId = "order-group",
            properties = "spring.json.value.default.type=com.smart_delivery_platform.order_service.dto.PaymentFailedEvent"
    )
    public void paymentFailed(PaymentFailedEvent event) {
        updateOrderStatus(event.getOrderId(), event.getStatus(), "payment-failed");
    }

    @KafkaListener(topics = "delivery-assigned", groupId = "order-group")
    public void consume(DeliveryAssignedEvent event) {
        updateOrderStatus(event.getOrderId(), event.getStatus(), "delivery-assigned");
        System.out.println("Order completed for orderId: " + event.getOrderId() +
                " and delivery partner is " + event.getDeliveryPartnerName());
    }
    @KafkaListener(topics ="restaurant-rejection" , groupId = "order-group",
            properties = "spring.json.value.default.type=com.smart_delivery_platform.order_service.dto.RestaurantRejectedEvent")
    public void orderRejection(RestaurantRejectedEvent event){
        updateOrderStatus(event.getOrderId(), event.getStatus(), "restaurant-rejection");
    }

    @KafkaListener(topics = "payment-refund-success" , groupId = "order-group",
            properties = "spring.json.value.default.type=com.smart_delivery_platform.order_service.dto.PaymentRefundedEvent")
    public void paymentRefundSuccess(PaymentRefundedEvent event){
        updateOrderStatus(event.getOrderId(), event.getStatus(), "payment-refund-success");
    }

    @KafkaListener(topics = "payment-refund-failure" , groupId = "order-group",
            properties = "spring.json.value.default.type=com.smart_delivery_platform.order_service.dto.PaymentRefundedEvent")
    public void paymentRefundFailure(PaymentRefundedEvent event){
        updateOrderStatus(event.getOrderId(), event.getStatus(), "payment-refund-failure");
    }

    @KafkaListener(topics = "refund-initiation" , groupId = "order-group",
            properties = "spring.json.value.default.type=com.smart_delivery_platform.order_service.dto.PaymentRefundedEvent")
    public void refundInitiationEvent(PaymentRefundedEvent event){
        updateOrderStatus(event.getOrderId(), event.getStatus(), "payment-refund-success");
    }


    private boolean updateOrderStatus(String orderId, String status, String eventName) {
        boolean updated = orderService.updateOrderStatus(orderId, status);
        if (!updated) {
            System.out.println("Skipping " + eventName + " event because order was not found: " + orderId);
        }
        return updated;
    }
}
