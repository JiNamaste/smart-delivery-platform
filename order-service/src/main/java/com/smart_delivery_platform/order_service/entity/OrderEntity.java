package com.smart_delivery_platform.order_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEntity {

    @Id
    private String orderId;

    private String customerId;
    private String restaurantId;
    private Double amount;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}