package com.smart_delivery_platform.tracking_service.controller;

import com.smart_delivery_platform.tracking_service.dto.OrderResponse;
import com.smart_delivery_platform.tracking_service.service.OrderFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tracking")
@RequiredArgsConstructor
public class TrackingController {

    private final OrderFeignClient orderFeignClient;

    @GetMapping("/{orderId}")
    public OrderResponse trackOrder(@PathVariable String orderId) {
        return orderFeignClient.getOrder(orderId);
    }
}