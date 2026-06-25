package com.smart_delivery_platform.restaurant_service.service;

import com.smart_delivery_platform.restaurant_service.dto.RestaurantConfirmedEvent;
import com.smart_delivery_platform.restaurant_service.dto.RestaurantRejectedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishRestaurantConfirmed(RestaurantConfirmedEvent event) {
        kafkaTemplate.send("restaurant-confirmed", event);
    }

    public void publishRestaurantRejected(RestaurantRejectedEvent restaurantRejectedEvent) {
        kafkaTemplate.send("restaurant-rejection", restaurantRejectedEvent);
    }
}
