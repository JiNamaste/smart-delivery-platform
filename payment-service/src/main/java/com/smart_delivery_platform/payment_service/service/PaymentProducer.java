package com.smart_delivery_platform.payment_service.service;

import com.smart_delivery_platform.payment_service.dto.PaymentRefundedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    void refundInitiationEvent(PaymentRefundedEvent paymentRefundedEvent){
          kafkaTemplate.send("refund-initiation",paymentRefundedEvent);
      }
}
