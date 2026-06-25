package com.smart_delivery_platform.order_service.controller;

import com.smart_delivery_platform.order_service.dto.OrderCreatedEvent;
import com.smart_delivery_platform.order_service.service.OrderProducer;
import org.springframework.web.bind.annotation.*;

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
    public String createOrder(@RequestBody OrderCreatedEvent orderCreatedEvent) {
        orderProducer.publishOrderEvent(orderCreatedEvent);
        return "Order Created Successfully";
    }
}
