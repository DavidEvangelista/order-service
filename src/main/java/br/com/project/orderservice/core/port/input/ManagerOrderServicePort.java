package br.com.project.orderservice.core.port.input;

import br.com.project.orderservice.core.domain.dto.OrderDto;
import br.com.project.orderservice.core.domain.enums.StatusOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ManagerOrderServicePort {
    void sendOrder(OrderDto order);
    void updateStatusOrder(String id, StatusOrder status);
    void createOrder(OrderDto orderDto);
    OrderDto getOrderById(UUID id);
    public Page<OrderDto> getOrdersByCustomerDocument(String document, Pageable pageable);
}
