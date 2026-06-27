package com.smart_delivery_platform.payment_service.service;

import com.smart_delivery_platform.payment_service.dto.*;
import com.smart_delivery_platform.payment_service.entity.PaymentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Random;

import static com.smart_delivery_platform.payment_service.service.IPaymentService.generateRandomPaymentId;

@Component
@RequiredArgsConstructor
public class PaymentConsumer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final IPaymentService paymentService;

    @Value("${app.event-delay-ms:100}")
    private long eventDelayMs;

    @KafkaListener(topics = "order-created", groupId = "payment-group")
    public void consume(OrderCreatedEvent orderevent) {
        applyEventDelay("payment processing", orderevent.getOrderId());
        String paymentId = null;
       PaymentEntity paymentEntity = paymentService.isOrderExist(orderevent.getOrderId());
       if(paymentEntity == null){
        paymentId = generateRandomPaymentId();
       }else {
           paymentId = paymentEntity.getPaymentId();
       }
       Random random = new Random();
        boolean paymentSuccess = random.nextBoolean();
        if(paymentSuccess){
            PaymentSuccessEvent successEvent = new PaymentSuccessEvent(orderevent.getOrderId(),
                    paymentId, orderevent.getAmount(), "PAYMENT-SUCCESS",orderevent.getRestaurantId());
            paymentService.save(successEvent);
            kafkaTemplate.send("payment-success", successEvent);
        } else {
            PaymentFailedEvent failedEvent = new PaymentFailedEvent(
                            orderevent.getOrderId(), "Insufficient Balance", "PAYMENT-FAILED", orderevent.getAmount(),paymentId);
            paymentService.save(failedEvent);
            kafkaTemplate.send("payment-failed", failedEvent);
        }

    }
    @KafkaListener(topics ="restaurant-rejection" , groupId = "payment-group",
            properties = "spring.json.value.default.type=com.smart_delivery_platform.payment_service.dto.RestaurantRejectedEvent")
    public void orderRejection(RestaurantRejectedEvent event){
        applyEventDelay("refund processing", event.getOrderId());
        Random random = new Random();
        boolean payment = random.nextBoolean();
        PaymentRefundedEvent paymentRefundedEvent =  new PaymentRefundedEvent().builder()
                .orderId(event.getOrderId())
                .refundAmount(event.getAmount())
                .paymentId(event.getPaymentId())
                .build();
        if(payment){
        paymentRefundedEvent.setStatus("REFUNDED-SUCCESS");
        paymentService.updatePaymentStatus(event.getOrderId(),"REFUND-SUCCESS");
        kafkaTemplate.send("payment-refund-success",paymentRefundedEvent);
        }else{
            paymentRefundedEvent.setStatus("REFUNDED_CANCELLED");
            paymentService.updatePaymentStatus(event.getOrderId(),"REFUND_FAILED");
            kafkaTemplate.send("payment-refund-failure",paymentRefundedEvent);
        }
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
