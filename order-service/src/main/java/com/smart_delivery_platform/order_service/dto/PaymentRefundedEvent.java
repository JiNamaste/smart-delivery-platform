package com.smart_delivery_platform.order_service.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRefundedEvent {
    private String orderId;
    private double refundAmount;
    private String status;
    private String paymentId;
}
