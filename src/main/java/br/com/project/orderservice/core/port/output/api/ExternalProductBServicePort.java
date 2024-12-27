package br.com.project.orderservice.core.port.output.api;

import br.com.project.orderservice.core.domain.dto.OrderDto;

public interface ExternalProductBServicePort {

    void createOrder(OrderDto order);

}
