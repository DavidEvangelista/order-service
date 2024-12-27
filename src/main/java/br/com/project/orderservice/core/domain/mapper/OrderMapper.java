package br.com.project.orderservice.core.domain.mapper;

import br.com.project.orderservice.core.domain.Customer;
import br.com.project.orderservice.core.domain.Order;
import br.com.project.orderservice.core.domain.Item;
import br.com.project.orderservice.core.domain.dto.CustomerDto;
import br.com.project.orderservice.core.domain.dto.OrderDto;
import br.com.project.orderservice.core.domain.dto.ItemDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(source = "items", target = "items")
    @Mapping(source = "customer", target = "customer")
    OrderDto toDto(Order order);

    @Mapping(source = "items", target = "items")
    @Mapping(source = "customer", target = "customer")
    Order toEntity(OrderDto orderDto);

    ItemDto itemToItemDto(Item item);

    List<ItemDto> itemsToItemDtos(List<Item> item);

    CustomerDto customerToCustomerDto(Customer customer);
}
