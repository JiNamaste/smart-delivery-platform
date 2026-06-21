package com.smart_delivery_platform.restaurant_service.service;

import com.smart_delivery_platform.restaurant_service.dto.PaymentSuccessEvent;
import com.smart_delivery_platform.restaurant_service.dto.RestaurantConfirmedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class RestaurantConsumer {

    private final RestaurantProducer restaurantProducer;

    public RestaurantConsumer(RestaurantProducer restaurantProducer) {
        this.restaurantProducer = restaurantProducer;
    }

    @KafkaListener(
            topics = "payment-success",
            groupId = "restaurant-group"
    )
    public void consume(PaymentSuccessEvent event) {
        System.out.println("Restaurant received payment success: " + event);
        System.out.println("Restaurant accepted order");

        RestaurantConfirmedEvent confirmedEvent = new RestaurantConfirmedEvent(event.getOrderId(),
                        "REST-101",
                        "CONFIRMED");
        restaurantProducer.publishRestaurantConfirmed(confirmedEvent);
    }
}