package com.smart_delivery_platform.order_service.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantAvailabilityService {

    private final RestaurantFeignClient feignClient;

    @CircuitBreaker(
            name = "restaurantService",
            fallbackMethod = "fallbackRestaurant"
    )
    public String checkRestaurant(String restaurantId) {
        return feignClient.checkAvailability(restaurantId);
    }

    public String fallbackRestaurant(String restaurantId, Exception ex) {
        return "RESTAURANT_SERVICE_DOWN";
    }

}