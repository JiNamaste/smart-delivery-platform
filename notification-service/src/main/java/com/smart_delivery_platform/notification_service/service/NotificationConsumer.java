package com.smart_delivery_platform.notification_service.service;

import com.smart_delivery_platform.notification_service.dto.PaymentFailedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {


    @KafkaListener(topics = "order-created", groupId = "notification-group")
    public void orderCreated(String event) {
        System.out.println("Notification: Order created -> " + event);
    }

    @KafkaListener(topics = "payment-success", groupId = "notification-group")
    public void paymentSuccess(String event) {
        System.out.println("Notification: Payment successful -> " + event);
    }

    @KafkaListener(topics = "restaurant-confirmed", groupId = "notification-group")
    public void restaurantConfirmed(String event) {
        System.out.println("Notification: Restaurant confirmed -> " + event);
    }

    @KafkaListener(topics = "delivery-assigned", groupId = "notification-group")
    public void deliveryAssigned(String event) {
        System.out.println("Notification: Delivery assigned -> " + event);
    }

    @KafkaListener(topics = "payment-failed", groupId = "notification-group")
    public void paymentFailed(String event) {
        System.out.println("Notification: Payment failed for order " + event);
    }

    @KafkaListener(topics = "restaurant-rejection", groupId = "notification-group")
    public void restaurantRejection(String event) {
        System.out.println("Notification: restaurant reject the order " + event);
    }


    @KafkaListener(topics = "payment-refund-success", groupId = "notification-group")
    public void paymentRefundSuccess(String event) {
        System.out.println("Notification: Payment Refunded " + event);
    }

    @KafkaListener(topics = "payment-refund-failure", groupId = "notification-group")
    public void paymentRefundFailure(String event) {
        System.out.println("Notification: Payment Refund Failed " + event);
    }
}
