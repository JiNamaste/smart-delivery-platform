package com.smart_delivery_platform.restaurant_service.service;

import com.smart_delivery_platform.restaurant_service.dto.PaymentSuccessEvent;
import com.smart_delivery_platform.restaurant_service.dto.RestaurantConfirmedEvent;
import com.smart_delivery_platform.restaurant_service.dto.RestaurantRejectedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RestaurantConsumer {

    private static final String[] CANCELLATION_REASONS = {
            "Restaurant is temporarily closed",
            "Item is out of stock",
            "Kitchen is overloaded",
            "Delivery partner unavailable",
            "Technical issue occurred",
            "Restaurant rejected the order",
            "Ingredients unavailable",
            "Unexpected maintenance issue",
            "High order volume",
            "Payment verification failed"
    };
    private final RestaurantProducer restaurantProducer;

    public RestaurantConsumer(RestaurantProducer restaurantProducer) {
        this.restaurantProducer = restaurantProducer;
    }

    @KafkaListener(
            topics = "payment-success",
            groupId = "restaurant-group"
    )
    public void consume(PaymentSuccessEvent event) {
        Random random = new Random();
        boolean accepted = random.nextBoolean();

        if(accepted){
        RestaurantConfirmedEvent confirmedEvent = new RestaurantConfirmedEvent(event.getOrderId(),
                        "REST-101", "RESTAURANT-CONFIRMED");
        restaurantProducer.publishRestaurantConfirmed(confirmedEvent);
        }else{
            RestaurantRejectedEvent restaurantRejectedEvent = new RestaurantRejectedEvent(event.getOrderId(),
                    "REST-101",getRandomCancellationReason(),"RESTAURANT-REJECTED", event.getAmount());
            restaurantProducer.publishRestaurantRejected(restaurantRejectedEvent);
        }
    }
    public static String getRandomCancellationReason() {
        Random random = new Random();
        int index = random.nextInt(CANCELLATION_REASONS.length);
        return CANCELLATION_REASONS[index];
    }
}