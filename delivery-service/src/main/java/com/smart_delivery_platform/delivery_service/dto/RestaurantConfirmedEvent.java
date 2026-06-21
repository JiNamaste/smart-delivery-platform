package com.smart_delivery_platform.delivery_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantConfirmedEvent {

    private String orderId;
    private String restaurantId;
    private String status;
}
