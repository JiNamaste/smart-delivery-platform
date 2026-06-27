package com.smart_delivery_platform.notification_service.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {

    private final EmailService emailService;

    public NotificationConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "order-created", groupId = "notification-group")
    public void orderCreated(String event) {
        sendNotification("Order Created", "Notification: Order created -> " + event);
    }

    @KafkaListener(topics = "payment-success", groupId = "notification-group")
    public void paymentSuccess(String event) {
        sendNotification("Payment Successful", "Notification: Payment successful -> " + event);
    }

    @KafkaListener(topics = "restaurant-confirmed", groupId = "notification-group")
    public void restaurantConfirmed(String event) {
        sendNotification("Restaurant Confirmed", "Notification: Restaurant confirmed -> " + event);
    }

    @KafkaListener(topics = "delivery-assigned", groupId = "notification-group")
    public void deliveryAssigned(String event) {
        sendNotification("Delivery Assigned", "Notification: Delivery assigned -> " + event);
    }

    @KafkaListener(topics = "payment-failed", groupId = "notification-group")
    public void paymentFailed(String event) {
        sendNotification("Payment Failed", "Notification: Payment failed for order " + event);
    }

    @KafkaListener(topics = "restaurant-rejection", groupId = "notification-group")
    public void restaurantRejection(String event) {
        sendNotification("Restaurant Rejected Order", "Notification: Restaurant rejected the order " + event);
    }


    @KafkaListener(topics = "payment-refund-success", groupId = "notification-group")
    public void paymentRefundSuccess(String event) {
        sendNotification("Payment Refunded", "Notification: Payment refunded " + event);
    }

    @KafkaListener(topics = "payment-refund-failure", groupId = "notification-group")
    public void paymentRefundFailure(String event) {
        sendNotification("Payment Refund Failed", "Notification: Payment refund failed " + event);
    }

    @KafkaListener(topics = "order-payment-success", groupId = "notification-group")
    public void orderPaymentSuccess(String event) {
        sendNotification("Order Payment Successful", "Notification: Payment success, restaurant can take order " + event);
    }

    private void sendNotification(String subject, String body) {
        System.out.println(body);
        emailService.sendNotification(subject, body);
    }
}
