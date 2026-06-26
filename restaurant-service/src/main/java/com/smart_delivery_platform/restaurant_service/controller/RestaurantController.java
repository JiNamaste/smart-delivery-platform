package com.smart_delivery_platform.restaurant_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    @GetMapping("/{restaurantId}/availability")
    public String checkAvailability(@PathVariable String restaurantId) {
        return "AVAILABLE";
    }
}
