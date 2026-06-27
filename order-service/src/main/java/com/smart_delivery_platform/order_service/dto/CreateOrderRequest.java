package com.smart_delivery_platform.order_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Payload used to place a new order.")
public class CreateOrderRequest {
    @Schema(description = "Customer placing the order.", example = "CUST-1001")
    private String customerId;

    @Schema(description = "Restaurant that should fulfill the order.", example = "REST-2001")
    private String restaurantId;

    @Schema(description = "Total order amount.", example = "549.50")
    private Double amount;

    @Schema(description = "Items included in the order.")
    private List<OrderItemRequest> items;
}
