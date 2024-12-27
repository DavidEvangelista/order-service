package br.com.project.orderservice.adapter.output.repository;

import br.com.project.orderservice.core.domain.Order;
import br.com.project.orderservice.core.port.output.repository.OrderRepositoryPort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface OrderRepository extends OrderRepositoryPort, MongoRepository<Order, UUID> {


}
