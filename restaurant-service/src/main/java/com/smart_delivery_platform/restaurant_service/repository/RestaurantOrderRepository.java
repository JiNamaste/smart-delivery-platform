package com.smart_delivery_platform.restaurant_service.repository;

import com.smart_delivery_platform.restaurant_service.entity.RestaurantOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestaurantOrderRepository extends JpaRepository<RestaurantOrderEntity, Long> {

    boolean existsByOrderId(String orderId);

    Optional<RestaurantOrderEntity> findByOrderId(String orderId);
}
