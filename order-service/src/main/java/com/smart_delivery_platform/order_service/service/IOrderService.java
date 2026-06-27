package com.smart_delivery_platform.order_service.service;

import com.smart_delivery_platform.order_service.dto.CreateOrderRequest;
import com.smart_delivery_platform.order_service.dto.OrderDTO;

public interface IOrderService {
    boolean updateOrderStatus(String orderId, String status);
    OrderDTO getOrder(String orderID);
    void saveOrder(CreateOrderRequest request, String orderId,String status);
}
