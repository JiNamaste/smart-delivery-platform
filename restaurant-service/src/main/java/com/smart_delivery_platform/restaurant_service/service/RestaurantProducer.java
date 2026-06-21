package com.smart_delivery_platform.restaurant_service.service;

import com.smart_delivery_platform.restaurant_service.dto.RestaurantConfirmedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantProducer {

    private final KafkaTemplate<String, RestaurantConfirmedEvent> kafkaTemplate;

    public void publishRestaurantConfirmed(RestaurantConfirmedEvent event) {
        kafkaTemplate.send("restaurant-confirmed", event);
        System.out.println("Restaurant confirmation published: " + event);
    }
}
