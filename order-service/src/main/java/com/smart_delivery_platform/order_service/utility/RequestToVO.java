package com.smart_delivery_platform.order_service.utility;

import com.smart_delivery_platform.order_service.dto.CreateOrderRequest;
import com.smart_delivery_platform.order_service.dto.OrderCreatedEvent;
import com.smart_delivery_platform.order_service.dto.OrderItemEvent;
import com.smart_delivery_platform.order_service.dto.OrderItemRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RequestToVO {

    public static OrderCreatedEvent requestToEvent(CreateOrderRequest orderRequest){
        OrderCreatedEvent orderCreatedEvent = OrderCreatedEvent.builder()
                .eventId("")
                .eventType("")
                .customerId(orderRequest.getCustomerId())
                .restaurantId(orderRequest.getRestaurantId())
                .createdAt(LocalDateTime.now())
                .amount(orderRequest.getAmount())
                .items(requestToEvent(orderRequest.getItems()))
                .build();
        return orderCreatedEvent;
    }
    public static List<OrderItemEvent> requestToEvent(List<OrderItemRequest> orderItemRequestList){
        List<OrderItemEvent> orderItemEvents = new ArrayList<>();
        for(OrderItemRequest orderItemRequests :orderItemRequestList ) {
            OrderItemEvent orderItemEvent = OrderItemEvent.builder().itemId(orderItemRequests.getItemId())
                    .quantity(orderItemRequests.getQuantity())
                    .price(orderItemRequests.getPrice())
                    .name(orderItemRequests.getName()).build();
            orderItemEvents.add(orderItemEvent);
        }
        return  orderItemEvents;
    }

    public static String generateRandomOrderId() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
