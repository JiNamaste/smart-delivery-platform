package com.smart_delivery_platform.order_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/orders")
public class OrderController {

    @GetMapping("/test")
    public String test() {
        return "Order Service is running";
    }
}
