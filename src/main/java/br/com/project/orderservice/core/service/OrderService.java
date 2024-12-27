package br.com.project.orderservice.core.service;

import br.com.project.orderservice.core.domain.Order;
import br.com.project.orderservice.core.domain.dto.ItemDto;
import br.com.project.orderservice.core.domain.dto.OrderDto;
import br.com.project.orderservice.core.domain.enums.StatusOrder;
import br.com.project.orderservice.core.exceptions.BaseException;
import br.com.project.orderservice.core.exceptions.NotFoundException;
import br.com.project.orderservice.core.port.input.ManagerOrderServicePort;
import br.com.project.orderservice.core.port.output.api.ExternalProductBServicePort;
import br.com.project.orderservice.core.port.output.repository.OrderRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService implements ManagerOrderServicePort {

    private final RabbitTemplate rabbitTemplate;
    private final OrderRepositoryPort repositoryPort;
    private final ExternalProductBServicePort productServicePort;
    private final OrderMapperService orderMapperService;

    public void sendOrder(OrderDto order) {
        try {
            log.info("Sending order to external product integration: {}", order);
            rabbitTemplate.convertAndSend("order.exchange", "create-order.routing.key", order);
        } catch (Exception e) {
            log.error("Failed to send order to RabbitMQ", e);
            throw new BaseException("Error sending order", e);
        }
    }

    public void createOrder(OrderDto orderDto) {
        if (orderDto == null || orderDto.getItems().isEmpty()) {
            throw new IllegalArgumentException("OrderDto or items cannot be null/empty");
        }

        log.info("Creating order: {}", orderDto);
        Order order = orderMapperService.toEntity(orderDto);
        order.setTotal(orderDto.getItems().stream().mapToDouble(ItemDto::getAmount).sum());
        order.setStatus(StatusOrder.CREATED);
        productServicePort.createOrder(orderMapperService.toDto(order));
        repositoryPort.save(order);
    }

    @Override
    public void updateStatusOrder(String id, StatusOrder status) {
        log.info("Updating order status: {}", id);
        repositoryPort.findById(UUID.fromString(id))
                .map(order -> {
                    order.setStatus(status);
                    repositoryPort.save(order);
                    return order;
                })
                .orElseThrow(() -> new NotFoundException("Order not found"));
    }

    @Cacheable(value = "orders", key = "#id")
    public OrderDto getOrderById(UUID id) {
        return repositoryPort.findById(id)
                .map(orderMapperService::toDto)
                .orElseThrow(() -> new NotFoundException("Order not found"));
    }

    @Cacheable(value = "customerOrders", key = "#document")
    public Page<OrderDto> getOrdersByCustomerDocument(String document, Pageable pageable) {
        Page<Order> orderPage = repositoryPort.findByCustomerDocument(document, pageable);
        if(orderPage == null || orderPage.isEmpty()) {
            return Page.empty();
        }
        return orderPage.map(orderMapperService::toDto);
    }

}
