package com.smart_delivery_platform.payment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentFailedEvent {

    private String orderId;
    private String reason;
    private String status;
    private Double amount;
    private String paymentId;
}