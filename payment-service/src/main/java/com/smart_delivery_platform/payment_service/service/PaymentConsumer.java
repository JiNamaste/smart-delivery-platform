package com.smart_delivery_platform.payment_service.service;

import com.smart_delivery_platform.payment_service.dto.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class PaymentConsumer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public PaymentConsumer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "order-created", groupId = "payment-group")
    public void consume(OrderCreatedEvent orderevent) {
        Random random = new Random();
        boolean paymentSuccess = random.nextBoolean();
        if(paymentSuccess){
            PaymentSuccessEvent successEvent = new PaymentSuccessEvent(orderevent.getOrderId(),
                    "PAY-101", orderevent.getAmount(), "PAYMENT-SUCCESS");
            kafkaTemplate.send("payment-success", successEvent);
        } else {
            PaymentFailedEvent failedEvent = new PaymentFailedEvent(
                            orderevent.getOrderId(), "Insufficient Balance", "PAYMENT-FAILED");
            kafkaTemplate.send("payment-failed", failedEvent);
        }
    }
    @KafkaListener(topics ="restaurant-rejection" , groupId = "payment-group",
            properties = "spring.json.value.default.type=com.smart_delivery_platform.payment_service.dto.RestaurantRejectedEvent")
    public void orderRejection(RestaurantRejectedEvent event){
        Random random = new Random();
        boolean payment = random.nextBoolean();
        PaymentRefundedEvent paymentRefundedEvent =  new PaymentRefundedEvent().builder().orderId(event.getOrderId())
                .refundAmount(event.getAmount()).build();
        if(payment){
        paymentRefundedEvent.setStatus("REFUNDED-SUCCESS");
        kafkaTemplate.send("payment-refund-success",paymentRefundedEvent);
        }else{
            paymentRefundedEvent.setStatus("REFUNDED_CANCELLED");
            kafkaTemplate.send("payment-refund-failure",paymentRefundedEvent);
        }
    }



}
