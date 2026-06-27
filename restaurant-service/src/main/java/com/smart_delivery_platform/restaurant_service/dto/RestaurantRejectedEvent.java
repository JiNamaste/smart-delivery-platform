package com.smart_delivery_platform.restaurant_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantRejectedEvent {

    private String orderId;
    private String restaurantId;
    private String reason;
    private String status;
    private Double amount;
    private String paymentId;

}
