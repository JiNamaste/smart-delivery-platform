package com.smart_delivery_platform.order_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Single item requested in an order.")
public class OrderItemRequest {
    @Schema(description = "Menu item identifier.", example = "ITEM-3001")
    private String itemId;

    @Schema(description = "Display name of the menu item.", example = "Paneer Butter Masala")
    private String name;

    @Schema(description = "Quantity ordered.", example = "2")
    private Integer quantity;

    @Schema(description = "Price per item.", example = "249.75")
    private Double price;
}
