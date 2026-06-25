package com.smart_delivery_platform.order_service.repository;

import com.smart_delivery_platform.order_service.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, String> {
}