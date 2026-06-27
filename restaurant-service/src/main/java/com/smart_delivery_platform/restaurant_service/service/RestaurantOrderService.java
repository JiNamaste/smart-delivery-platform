package com.smart_delivery_platform.restaurant_service.service;

import com.smart_delivery_platform.restaurant_service.dto.PaymentSuccessEvent;
import com.smart_delivery_platform.restaurant_service.entity.RestaurantOrderEntity;
import com.smart_delivery_platform.restaurant_service.repository.RestaurantOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RestaurantOrderService {

    private final RestaurantOrderRepository restaurantOrderRepository;

    @Transactional
    public boolean saveDecisionIfAbsent(PaymentSuccessEvent event, String status, String rejectionReason) {
        if (restaurantOrderRepository.existsByOrderId(event.getOrderId())) {
            return false;
        }

        try {
            RestaurantOrderEntity restaurantOrder = RestaurantOrderEntity.builder()
                    .orderId(event.getOrderId())
                    .restaurantId(event.getRestaurantId())
                    .paymentId(event.getPaymentId())
                    .amount(event.getAmount())
                    .status(status)
                    .rejectionReason(rejectionReason)
                    .build();
            restaurantOrderRepository.save(restaurantOrder);
            return true;
        } catch (DataIntegrityViolationException ex) {
            return false;
        }
    }
}
