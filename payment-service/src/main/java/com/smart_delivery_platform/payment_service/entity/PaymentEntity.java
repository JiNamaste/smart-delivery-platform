package com.smart_delivery_platform.payment_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "payments",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_payments_payment_id", columnNames = "payment_id"),
                @UniqueConstraint(name = "uk_payments_order_id", columnNames = "order_id")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "payment_id", unique = true)
    private String paymentId;

    @Column(name = "order_id", unique = true)
    private String orderId;
    private Double amount;

    private String paymentStatus;
    // SUCCESS, FAILED

    private String refundStatus;
    // NOT_REQUIRED, REFUND_PENDING, REFUNDED, REFUND_FAILED

    private String failureReason;
    private String refundId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
