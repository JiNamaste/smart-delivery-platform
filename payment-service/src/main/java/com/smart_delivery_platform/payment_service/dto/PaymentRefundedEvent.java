package com.smart_delivery_platform.payment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRefundedEvent {
    private String orderId;
    private double refundAmount;
    private String status;
    private String paymentId;
}