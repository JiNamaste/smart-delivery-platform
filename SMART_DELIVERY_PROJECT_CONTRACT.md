# Smart Delivery Orchestration Platform - Project Contract

## 1. Project Goal

Build a real-world microservices-based delivery order system inspired by food/grocery delivery platforms.

The system should simulate this flow:

Customer places order -> payment is processed -> restaurant confirms/rejects -> delivery partner is assigned -> customer receives notifications -> order status is tracked.

This project is designed to learn and demonstrate:

- Apache Kafka
- Spring Boot Microservices
- API Gateway
- Eureka Service Discovery
- Resilience4j
- Circuit Breaker Pattern
- Saga Design Pattern
- Event-driven architecture
- Distributed failure handling

---

## 2. Business Problem

In delivery platforms, multiple services must work together:

- Order creation
- Payment processing
- Restaurant confirmation
- Delivery assignment
- Notification
- Tracking

If one service fails, the system should not completely break. It should retry, fallback, cancel the order, or refund payment where required.

This project solves that problem using asynchronous Kafka events and Saga compensation.

---

## 3. Microservices

## 3.1 Eureka Server

### Responsibility

Service registry where all microservices register themselves.

### Port

```text
8761
```

### Dependencies

- Spring Cloud Netflix Eureka Server

---

## 3.2 API Gateway

### Responsibility

Single entry point for all external requests.

Routes requests to internal services using Eureka.

### Port

```text
8080
```

### Routes

```text
/api/orders/**        -> order-service
/api/payments/**      -> payment-service
/api/restaurants/**   -> restaurant-service
/api/delivery/**      -> delivery-service
/api/tracking/**      -> tracking-service
```

### Dependencies

- Spring Cloud Gateway
- Eureka Client

---

## 3.3 Order Service

### Responsibility

- Create new order
- Store order status
- Publish `ORDER_CREATED` event
- Listen to payment, restaurant, and delivery events
- Update order lifecycle status
- Trigger order cancellation when failure happens

### Port

```text
8081
```

### Database Table

```text
orders
```

### Main APIs

```http
POST /api/orders
GET /api/orders/{orderId}
GET /api/orders/{orderId}/status
```

### Kafka Events Produced

```text
order-created
order-cancelled
```

### Kafka Events Consumed

```text
payment-success
payment-failed
restaurant-confirmed
restaurant-rejected
delivery-assigned
```

---

## 3.4 Payment Service

### Responsibility

- Listen to order-created event
- Process mock payment
- Publish payment success/failure
- Refund payment if restaurant rejects order

### Port

```text
8082
```

### Kafka Events Consumed

```text
order-created
restaurant-rejected
order-cancelled
```

### Kafka Events Produced

```text
payment-success
payment-failed
payment-refunded
```

---

## 3.5 Restaurant Service

### Responsibility

- Check restaurant availability
- Accept or reject order
- Publish confirmation/rejection event

### Port

```text
8083
```

### Kafka Events Consumed

```text
payment-success
```

### Kafka Events Produced

```text
restaurant-confirmed
restaurant-rejected
```

---

## 3.6 Delivery Service

### Responsibility

- Assign delivery partner after restaurant confirmation
- Publish delivery assignment event

### Port

```text
8084
```

### Kafka Events Consumed

```text
restaurant-confirmed
```

### Kafka Events Produced

```text
delivery-assigned
```

---

## 3.7 Notification Service

### Responsibility

- Listen to all important events
- Send mock notification by logging message

### Port

```text
8085
```

### Kafka Events Consumed

```text
order-created
payment-success
payment-failed
restaurant-confirmed
restaurant-rejected
delivery-assigned
order-cancelled
payment-refunded
```

---

## 3.8 Tracking Service

### Responsibility

- Track order status
- Expose latest order journey
- Consume delivery and order events

### Port

```text
8086
```

### APIs

```http
GET /api/tracking/{orderId}
```

### Kafka Events Consumed

```text
order-created
restaurant-confirmed
delivery-assigned
order-cancelled
```

---

# 4. Kafka Contract

## 4.1 Topic: order-created

### Producer

Order Service

### Consumers

Payment Service, Notification Service, Tracking Service

### Event Payload

```json
{
  "eventId": "evt-1001",
  "eventType": "ORDER_CREATED",
  "orderId": "ORD-1001",
  "customerId": "CUST-101",
  "restaurantId": "REST-501",
  "amount": 599.00,
  "items": [
    {
      "itemId": "ITEM-1",
      "name": "Paneer Roll",
      "quantity": 2,
      "price": 199.00
    }
  ],
  "createdAt": "2026-06-18T10:30:00"
}
```

---

## 4.2 Topic: payment-success

### Producer

Payment Service

### Consumers

Order Service, Restaurant Service, Notification Service

### Event Payload

```json
{
  "eventId": "evt-2001",
  "eventType": "PAYMENT_SUCCESS",
  "orderId": "ORD-1001",
  "paymentId": "PAY-9001",
  "amount": 599.00,
  "status": "SUCCESS",
  "createdAt": "2026-06-18T10:31:00"
}
```

---

## 4.3 Topic: payment-failed

### Producer

Payment Service

### Consumers

Order Service, Notification Service

### Event Payload

```json
{
  "eventId": "evt-2002",
  "eventType": "PAYMENT_FAILED",
  "orderId": "ORD-1001",
  "reason": "Insufficient balance",
  "status": "FAILED",
  "createdAt": "2026-06-18T10:31:00"
}
```

---

## 4.4 Topic: restaurant-confirmed

### Producer

Restaurant Service

### Consumers

Order Service, Delivery Service, Notification Service, Tracking Service

### Event Payload

```json
{
  "eventId": "evt-3001",
  "eventType": "RESTAURANT_CONFIRMED",
  "orderId": "ORD-1001",
  "restaurantId": "REST-501",
  "estimatedPreparationTime": 20,
  "status": "CONFIRMED",
  "createdAt": "2026-06-18T10:33:00"
}
```

---

## 4.5 Topic: restaurant-rejected

### Producer

Restaurant Service

### Consumers

Order Service, Payment Service, Notification Service

### Event Payload

```json
{
  "eventId": "evt-3002",
  "eventType": "RESTAURANT_REJECTED",
  "orderId": "ORD-1001",
  "restaurantId": "REST-501",
  "reason": "Restaurant overloaded",
  "status": "REJECTED",
  "createdAt": "2026-06-18T10:33:00"
}
```

---

## 4.6 Topic: delivery-assigned

### Producer

Delivery Service

### Consumers

Order Service, Notification Service, Tracking Service

### Event Payload

```json
{
  "eventId": "evt-4001",
  "eventType": "DELIVERY_ASSIGNED",
  "orderId": "ORD-1001",
  "deliveryPartnerId": "DP-701",
  "deliveryPartnerName": "Rahul",
  "estimatedDeliveryTime": 35,
  "status": "ASSIGNED",
  "createdAt": "2026-06-18T10:35:00"
}
```

---

## 4.7 Topic: order-cancelled

### Producer

Order Service

### Consumers

Payment Service, Notification Service, Tracking Service

### Event Payload

```json
{
  "eventId": "evt-5001",
  "eventType": "ORDER_CANCELLED",
  "orderId": "ORD-1001",
  "reason": "Payment failed or restaurant rejected",
  "status": "CANCELLED",
  "createdAt": "2026-06-18T10:36:00"
}
```

---

## 4.8 Topic: payment-refunded

### Producer

Payment Service

### Consumers

Order Service, Notification Service

### Event Payload

```json
{
  "eventId": "evt-6001",
  "eventType": "PAYMENT_REFUNDED",
  "orderId": "ORD-1001",
  "paymentId": "PAY-9001",
  "refundId": "REF-3001",
  "amount": 599.00,
  "status": "REFUNDED",
  "createdAt": "2026-06-18T10:38:00"
}
```

---

# 5. Order Status Lifecycle

```text
CREATED
PAYMENT_PENDING
PAYMENT_SUCCESS
PAYMENT_FAILED
RESTAURANT_PENDING
RESTAURANT_CONFIRMED
RESTAURANT_REJECTED
DELIVERY_PENDING
DELIVERY_ASSIGNED
COMPLETED
CANCELLED
REFUNDED
PENDING_RETRY
```

---

# 6. Saga Design Pattern Contract

## 6.1 Success Saga

```text
1. Order Service creates order
2. Order Service publishes ORDER_CREATED
3. Payment Service consumes ORDER_CREATED
4. Payment Service publishes PAYMENT_SUCCESS
5. Restaurant Service consumes PAYMENT_SUCCESS
6. Restaurant Service publishes RESTAURANT_CONFIRMED
7. Delivery Service consumes RESTAURANT_CONFIRMED
8. Delivery Service publishes DELIVERY_ASSIGNED
9. Order Service marks order as COMPLETED
```

---

## 6.2 Payment Failure Saga

```text
1. Order Service creates order
2. Payment Service fails payment
3. Payment Service publishes PAYMENT_FAILED
4. Order Service marks order as CANCELLED
5. Notification Service sends failure notification
```

---

## 6.3 Restaurant Rejection Compensation Saga

```text
1. Order Service creates order
2. Payment Service processes payment successfully
3. Restaurant Service rejects order
4. Restaurant Service publishes RESTAURANT_REJECTED
5. Payment Service consumes RESTAURANT_REJECTED
6. Payment Service refunds amount
7. Payment Service publishes PAYMENT_REFUNDED
8. Order Service marks order as REFUNDED/CANCELLED
```

---

# 7. Resilience4j Contract

## Circuit Breaker Use Case

Order Service may call Restaurant Service synchronously to check availability before creating order.

If Restaurant Service is down:

```text
Circuit breaker opens
Fallback method returns default response
Order status becomes PENDING_RETRY
```

## Example Fallback Response

```json
{
  "orderId": "ORD-1001",
  "status": "PENDING_RETRY",
  "message": "Restaurant service is temporarily unavailable. Order will be retried."
}
```

## Resilience4j Features

```text
Circuit Breaker
Retry
Rate Limiter
Fallback
```

---

# 8. Minimum Completion Criteria

The project is considered complete when:

- Eureka Server is running
- API Gateway routes requests
- Order Service creates order
- Kafka event is published after order creation
- Payment Service consumes order event
- Payment Service publishes success/failure event
- Restaurant Service confirms/rejects order
- Delivery Service assigns delivery partner
- Notification Service logs event messages
- Saga failure flow works
- Resilience4j fallback works
- README contains architecture and run steps
- Docker Compose starts Kafka and databases

---

# 9. Tech Stack

```text
Java 21
Spring Boot
Spring Cloud Gateway
Spring Cloud Netflix Eureka
Apache Kafka
Spring Kafka
PostgreSQL
Resilience4j
Docker
Docker Compose
Swagger/OpenAPI
Maven
Lombok
```
