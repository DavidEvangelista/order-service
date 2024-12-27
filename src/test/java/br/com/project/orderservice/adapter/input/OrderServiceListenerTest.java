package br.com.project.orderservice.adapter.input;

import br.com.project.orderservice.adapter.output.api.ExternalProductIntegrationClient;
import br.com.project.orderservice.core.domain.Order;
import br.com.project.orderservice.core.domain.dto.CustomerDto;
import br.com.project.orderservice.core.domain.dto.ItemDto;
import br.com.project.orderservice.core.domain.dto.OrderDto;
import br.com.project.orderservice.core.domain.mapper.OrderMapper;
import br.com.project.orderservice.core.port.output.repository.OrderRepositoryPort;
import br.com.project.orderservice.core.service.OrderMapperService;
import br.com.project.orderservice.core.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceListenerTest {

    @InjectMocks
    private OrderService orderService;

    @InjectMocks
    OrderServiceListener orderServiceListener;

    @InjectMocks
    private OrderMapperService orderMapperService;

    @Mock
    RabbitTemplate rabbitTemplate;
    @Mock
    private ExternalProductIntegrationClient productService;
    @Mock
    private OrderRepositoryPort repository;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(rabbitTemplate, repository, productService, orderMapperService);
        orderServiceListener = new OrderServiceListener(orderService);
    }

    @Test
    void listenCreateOrderQueue() {
        OrderDto order = OrderDto
                .builder()
                .items(List.of(ItemDto
                                .builder()
                                .sku("prod-123")
                                .name("item-1")
                                .price(100.00)
                                .quantity(2)
                                .build(),
                        ItemDto
                                .builder()
                                .sku("prod-124")
                                .name("item-2")
                                .quantity(1)
                                .price(200.00)
                                .build()))
                .customer(CustomerDto
                        .builder()
                        .name("cust-456")
                        .document("12345678901")
                        .build())
                .createdAt(LocalDateTime.now())
                .build();

        when(repository.save(any(Order.class))).thenReturn(OrderMapper.INSTANCE.toEntity(order));

        assertDoesNotThrow(()->
                orderServiceListener.listenCreateOrderQueue(order)
        );
    }
}