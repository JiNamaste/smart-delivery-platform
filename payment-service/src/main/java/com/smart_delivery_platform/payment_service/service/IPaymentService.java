package com.smart_delivery_platform.payment_service.service;

import com.smart_delivery_platform.payment_service.dto.PaymentFailedEvent;
import com.smart_delivery_platform.payment_service.dto.PaymentSuccessEvent;
import com.smart_delivery_platform.payment_service.entity.PaymentEntity;

import java.util.UUID;

public interface IPaymentService {
    void  save(PaymentSuccessEvent event);
    void  save(PaymentFailedEvent event);
    PaymentEntity isOrderExist(String orderId);
    boolean updatePaymentStatus(String orderId, String status);
    String refundInitiation(String orderId);

    static String generateRandomPaymentId() {
        return "PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
