package br.com.project.orderservice.core.service;

import br.com.project.orderservice.core.domain.Order;
import br.com.project.orderservice.core.domain.dto.OrderDto;
import br.com.project.orderservice.core.domain.mapper.OrderMapper;
import org.springframework.stereotype.Service;

@Service
public class OrderMapperService {

    public Order toEntity(OrderDto dto) {
        return OrderMapper.INSTANCE.toEntity(dto);
    }

    public OrderDto toDto(Order entity) {
        return OrderMapper.INSTANCE.toDto(entity);
    }

}
