package com.smart_delivery_platform.restaurant_service.service;

import com.smart_delivery_platform.restaurant_service.dto.PaymentSuccessEvent;
import com.smart_delivery_platform.restaurant_service.dto.RestaurantConfirmedEvent;
import com.smart_delivery_platform.restaurant_service.dto.RestaurantRejectedEvent;
import org.springframework.beans.factory.annotation.Value;
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
    private final RestaurantOrderService restaurantOrderService;

    @Value("${app.event-delay-ms:300}")
    private long eventDelayMs;

    public RestaurantConsumer(RestaurantProducer restaurantProducer, RestaurantOrderService restaurantOrderService) {
        this.restaurantProducer = restaurantProducer;
        this.restaurantOrderService = restaurantOrderService;
    }

    @KafkaListener(
            topics = "order-payment-success",
            groupId = "restaurant-group"
    )
    public void consume(PaymentSuccessEvent event) {
        applyEventDelay("restaurant confirmation", event.getOrderId());
        Random random = new Random();
        boolean accepted = random.nextBoolean();

        if(accepted){
            String status = "RESTAURANT-CONFIRMED";
            boolean saved = restaurantOrderService.saveDecisionIfAbsent(event, status, null);
            if (!saved) {
                return;
            }

            RestaurantConfirmedEvent confirmedEvent = new RestaurantConfirmedEvent(event.getOrderId(),
                            event.getRestaurantId(), status);
            restaurantProducer.publishRestaurantConfirmed(confirmedEvent);
        }else{
            String status = "RESTAURANT-REJECTED";
            String rejectionReason = getRandomCancellationReason();
            boolean saved = restaurantOrderService.saveDecisionIfAbsent(event, status, rejectionReason);
            if (!saved) {
                return;
            }

            RestaurantRejectedEvent restaurantRejectedEvent = new RestaurantRejectedEvent(event.getOrderId(),
                    event.getRestaurantId(), rejectionReason, status, event.getAmount(), event.getPaymentId());
            restaurantProducer.publishRestaurantRejected(restaurantRejectedEvent);
        }
    }
    public static String getRandomCancellationReason() {
        Random random = new Random();
        int index = random.nextInt(CANCELLATION_REASONS.length);
        return CANCELLATION_REASONS[index];
    }

    private void applyEventDelay(String stage, String orderId) {
        if (eventDelayMs <= 0) {
            return;
        }

        try {
            System.out.println("Delaying " + stage + " for order " + orderId + " by " + eventDelayMs + " ms");
            Thread.sleep(eventDelayMs);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}
