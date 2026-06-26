package com.smart_delivery_platform.order_service.service;

import com.smart_delivery_platform.order_service.dto.OrderCreatedEvent;
import com.smart_delivery_platform.order_service.entity.OrderEntity;
import com.smart_delivery_platform.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderProducer {

    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;
    private final OrderRepository orderRepository;
    private final RestaurantAvailabilityService restaurantAvailabilityService;

    public void publishOrderEvent(OrderCreatedEvent event) {
        if (event == null || event.getOrderId() == null) {
            throw new IllegalArgumentException("Order request and orderId are required");
        }

        OrderEntity order = OrderEntity.builder()
                .orderId(event.getOrderId())
                .amount(event.getAmount())
                .createdAt(LocalDateTime.now())
                .status("created")
                .build();

        String response = restaurantAvailabilityService.checkRestaurant("REST-101");
        if(response.equals("RESTAURANT_SERVICE_DOWN")){
            order.setStatus("PENDING_RETRY");
            orderRepository.save(order);
            //return "Restaurant Service Down";
        }

        orderRepository.save(order);
        kafkaTemplate.send("order-created", event);
        System.out.println("Order event published: " + event);
    }
}
