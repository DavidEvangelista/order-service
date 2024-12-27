# Order Service

## Description
The Order Service is a microservice designed to handle order management in an asynchronous architecture. It uses RabbitMQ for message processing, MongoDB for data persistence, and Redis for caching. The service exposes a REST API for querying orders by their ID or by customer document, with full Swagger documentation.

---

## Features
- **Order Creation**: Orders are created asynchronously by consuming messages from RabbitMQ.
- **Order Retrieval**: Provides APIs to retrieve orders by ID and by customer document, with Redis caching for optimized performance.
- **Order Status Update**: Allows updating the status of an order.
- **Integration**: External product service integration.

---

## Technologies Used
- **Java 21**
- **Spring Boot**
- **Spring Data MongoDB**
- **RabbitMQ**
- **Redis**
- **Swagger/OpenAPI 3**
- **JUnit 5 and MockMvc**

---

## Prerequisites

- Java 21
- Docker and Docker Compose

---

## Getting Started

### Clone the Repository
```bash
git clone https://github.com/DavidEvangelista/order-service.git
cd order-service
```

### Build the Application
For production profile:
```bash
./gradlew build -Pprofile=prd
```

For development profile:
```bash
./gradlew build -Pprofile=dev
```

### Run the Application with Docker Compose
```bash
docker-compose up --build
```

This will start the application along with RabbitMQ, MongoDB, and Redis.

---

## API Documentation
Access the API documentation at:
```
http://localhost:8080/swagger-ui.html
```

---

## Endpoints

### Order Endpoints

#### Create Order (asynchronous)
- **Queue**: `create-order.queue`
- **Exchange**: `order.exchange`
- **Routing Key**: `create-order.routing.key`

#### Get Order by ID
- **GET** `/orders/{id}`

#### Get Orders by Customer Document
- **GET** `/orders/customer/{document}`

#### Update Order Status
- **PUT** `/orders/{id}/status`

---

## Environment Configuration

### application-dev.yml
Example configuration for development:
```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/orders_db
      password: admin
      username: admin

rabbitmq:
  host: localhost
  port: 5672
  username: guest
  password: guest

redis:
  host: localhost
  port: 6379
```

---

## Testing

### Run Unit Tests
```bash
./gradlew test
```

The project includes unit tests for the controller using MockMvc and service-layer tests for business logic validation.

---

## Contact
For questions or support, please contact [David Zoroastro Evangelista] at [david.zoroastro.evangelista@gmail.com].

