# Smart Delivery Platform

Smart Delivery Platform is a Spring Boot microservices project that simulates a food delivery order flow using Kafka-based asynchronous communication.

The main flow is:

```text
Customer places order -> payment is processed -> restaurant confirms/rejects -> delivery partner is assigned -> notification is sent -> order can be tracked
```

## Tech Stack

- Java 17+
- Spring Boot
- Spring Cloud Gateway
- Eureka Service Discovery
- Apache Kafka
- Spring Kafka
- PostgreSQL
- Resilience4j
- Maven
- Docker Compose
- Swagger/OpenAPI

## Services

| Service | Port | Responsibility |
| --- | ---: | --- |
| Eureka Server | 8761 | Service registry |
| API Gateway | 9090 | Single entry point for APIs |
| Order Service | 8085 | Create orders and manage order status |
| Payment Service | 8082 | Mock payment and refund flow |
| Restaurant Service | 8083 | Restaurant availability and order confirmation |
| Delivery Service | 8084 | Delivery partner assignment |
| Notification Service | 8086 | Kafka event notifications and email |
| Tracking Service | 8089 | Order tracking view |

## Architecture

The platform uses Kafka events to coordinate a Saga-style workflow between services.

Important topics:

- `order-created`
- `payment-success`
- `payment-failed`
- `restaurant-confirmed`
- `restaurant-rejection`
- `delivery-assigned`
- `payment-refund-success`
- `payment-refund-failure`
- `order-payment-success`

## API Gateway Routes

Base URL:

```text
http://localhost:9090
```

Routes:

| Route | Service |
| --- | --- |
| `/api/orders/**` | Order Service |
| `/api/payments/**` | Payment Service |
| `/api/tracking/**` | Tracking Service |

Swagger UI:

```text
http://localhost:9090/swagger-ui.html
```

## Prerequisites

Install:

- JDK 17 or newer
- Docker Desktop
- PostgreSQL
- Maven, or use the included Maven wrapper scripts

Create these PostgreSQL databases locally:

```sql
CREATE DATABASE smart_delivery_order_db;
CREATE DATABASE smart_delivery_payment_service;
CREATE DATABASE smart_delivery_restaurant_service;
```

The current service configs use:

```text
username: postgres
password: 123
```

Update each service `application.yml` if your local PostgreSQL username or password is different.

## Start Kafka

From the project root:

```powershell
docker compose up -d
```

This starts:

- Zookeeper on `2181`
- Kafka on `9092`

## Notification Email Setup

The notification service reads email settings from:

```text
notification-service/.env
```

Example:

```properties
MAIL_USERNAME=yourgmail@gmail.com
MAIL_APP_PASSWORD=your-16-digit-gmail-app-password
NOTIFICATION_TO_EMAIL=receiver@gmail.com
```

Use a Gmail App Password, not your real Gmail password.

To create one:

1. Enable 2-Step Verification on your Google account.
2. Open Google Account Security settings.
3. Create an App Password.
4. Put the 16-character app password in `MAIL_APP_PASSWORD`.

The `.env` file is ignored by Git.

## Run Services

Start services in this order:

1. Eureka Server
2. API Gateway
3. Order Service
4. Payment Service
5. Restaurant Service
6. Delivery Service
7. Notification Service
8. Tracking Service

Run each service from its own folder:

```powershell
cd eureka-server
.\mvnw spring-boot:run
```

Example for notification service:

```powershell
cd notification-service
.\mvnw spring-boot:run
```

If Maven cannot find Java, set `JAVA_HOME` first:

```powershell
$env:JAVA_HOME="C:\Program Files\Java\jdk-21.0.11"
```

## Create an Order

Send the request through the API Gateway:

```http
POST http://localhost:9090/api/orders
Content-Type: application/json
```

Body:

```json
{
  "customerId": "CUST-1001",
  "restaurantId": "REST-2001",
  "amount": 549.50,
  "items": [
    {
      "itemId": "ITEM-1",
      "name": "Paneer Roll",
      "quantity": 2,
      "price": 199.00
    }
  ]
}
```

Expected response:

```text
Order Created Successfully
```

After the order is created, Kafka events drive payment, restaurant confirmation, delivery assignment, and notification.

## Useful Endpoints

| Method | Endpoint | Description |
| --- | --- | --- |
| GET | `/api/orders/test` | Check order service |
| POST | `/api/orders` | Create order |
| GET | `/api/orders/{orderId}` | Get order details |
| POST | `/api/orders/{orderId}/retry-payment` | Retry payment |
| POST | `/api/payments/{orderId}/retry-refund` | Retry refund |
| GET | `/api/tracking/{orderId}` | Track order |
| GET | `/api/restaurants/{restaurantId}/availability` | Check restaurant availability |

## Development Notes

- Notification emails are currently sent to one configured test email address.
- To send to the actual customer, add `customerEmail` to order request/event payloads and use that value in notification-service.
- Docker Compose currently starts Kafka and Zookeeper only. PostgreSQL must be running separately unless database services are added to `docker-compose.yml`.
- Service ports are based on the current `application.yml` files.
