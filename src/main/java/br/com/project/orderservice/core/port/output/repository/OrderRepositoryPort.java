package br.com.project.orderservice.core.port.output.repository;

import br.com.project.orderservice.core.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepositoryPort {

    Order save(Order order);

    Optional<Order> findById(UUID id);

    Page<Order> findByCustomerDocument(String document, Pageable pageable);

}
