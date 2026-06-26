package com.smart_delivery_platform.order_service.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "restaurant-service")
public interface RestaurantFeignClient {

    @GetMapping("/api/restaurants/{restaurantId}/availability")
    String checkAvailability(@PathVariable String restaurantId);
}
