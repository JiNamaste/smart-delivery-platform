package com.smart_delivery_platform.payment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentSuccessEvent {

    private String orderId;
    private String paymentId;
    private Double amount;
    private String status;
}