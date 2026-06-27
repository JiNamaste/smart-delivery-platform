package com.smart_delivery_platform.order_service.service;

import com.smart_delivery_platform.order_service.dto.CreateOrderRequest;
import com.smart_delivery_platform.order_service.dto.OrderDTO;
import com.smart_delivery_platform.order_service.entity.OrderEntity;
import com.smart_delivery_platform.order_service.entity.OrderItemEntity;
import com.smart_delivery_platform.order_service.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService  implements IOrderService{
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public boolean updateOrderStatus(String orderId, String status) {
        OrderEntity order = orderRepository.findByOrderId(orderId).orElse(null);
        if (order == null) {
            return false;
        }
        order.setStatus(status);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);
        return true;
    }

    public OrderDTO getOrder(String orderID){
        OrderEntity order = orderRepository.findByOrderId(orderID)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        OrderDTO orderDTO = OrderDTO.builder().orderId(order.getOrderId())
                .amount(order.getAmount())
                .status(order.getStatus())
                .restaurantId(order.getRestaurantId())
                .updatedAt(order.getUpdatedAt())
                .build();
        return orderDTO;
    }
      public void saveOrder(CreateOrderRequest request, String orderId ,String status){
          OrderEntity order = OrderEntity.builder()
                  .orderId(orderId)
                  .customerId(request.getCustomerId())
                  .restaurantId(request.getRestaurantId())
                  .amount(request.getAmount())
                  .status("CREATED")
                  .createdAt(LocalDateTime.now())
                  .updatedAt(LocalDateTime.now())
                  .build();

          List<OrderItemEntity> items = request.getItems()
                  .stream()
                  .map(item -> OrderItemEntity.builder()
                          .itemId(item.getItemId())
                          .name(item.getName())
                          .quantity(item.getQuantity())
                          .price(item.getPrice())
                          .order(order)
                          .build())
                  .toList();

          order.setItems(items);
          if(status != null){ order.setStatus(status);}
          orderRepository.save(order);
    }

}
