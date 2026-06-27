package com.smart_delivery_platform.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderCreatedEvent {
    private String eventId;
    private String eventType;
    private String orderId;
    private String customerId;
    private String restaurantId;
    private Double amount;
    private List<OrderItemEvent> items;
    private LocalDateTime createdAt;
}