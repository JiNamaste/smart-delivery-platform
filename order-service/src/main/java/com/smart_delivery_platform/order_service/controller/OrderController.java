package com.smart_delivery_platform.order_service.controller;

import com.smart_delivery_platform.order_service.dto.OrderCreatedEvent;
import com.smart_delivery_platform.order_service.service.OrderProducer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderProducer orderProducer;

    public OrderController(OrderProducer orderProducer) {
        this.orderProducer = orderProducer;
    }

    @GetMapping("/test")
    public String test() {
        return "Order Service is running";
    }

    @PostMapping
    public String createOrder() {

        OrderCreatedEvent event = new OrderCreatedEvent("ORD-101", "Ujjwal", 600.0);
        orderProducer.publishOrderEvent(event);
        return "Order Created Successfully";
    }
}
