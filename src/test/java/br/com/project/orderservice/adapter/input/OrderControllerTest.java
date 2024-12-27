package br.com.project.orderservice.adapter.input;

import br.com.project.orderservice.adapter.output.api.ExternalProductIntegrationClient;
import br.com.project.orderservice.core.domain.Customer;
import br.com.project.orderservice.core.domain.Item;
import br.com.project.orderservice.core.domain.Order;
import br.com.project.orderservice.core.domain.dto.CustomerDto;
import br.com.project.orderservice.core.domain.dto.ItemDto;
import br.com.project.orderservice.core.domain.dto.OrderDto;
import br.com.project.orderservice.core.domain.enums.StatusOrder;
import br.com.project.orderservice.core.domain.mapper.OrderMapper;
import br.com.project.orderservice.core.port.output.repository.OrderRepositoryPort;
import br.com.project.orderservice.core.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@ContextConfiguration(classes = OrderController.class)
@Import({OrderService.class})
class OrderControllerTest {

    private static final String BASE_URL = "/v1/orders";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrderRepositoryPort repository;

    @MockitoBean
    private RabbitTemplate rabbitTemplate;

    @MockitoBean
    private ExternalProductIntegrationClient client;

    @Test
    void testPostSendOrder_Success() throws Exception {
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

        mockMvc.perform(post(BASE_URL)
                        .content(writeJsonToString(order))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                .andExpect(status().isAccepted());
    }

    @Test
    void testPostSendInvalidOrder_Error() throws Exception {
        OrderDto order = OrderDto
                .builder()
                .items(List.of(ItemDto
                                .builder()
                                .sku("prod-123")
                                .name("item-1")
                                .price(100.00)
                                .build(),
                        ItemDto
                                .builder()
                                .sku("prod-124")
                                .name("item-2")
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

        mockMvc.perform(post(BASE_URL)
                        .content(writeJsonToString(order))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isAccepted());
    }

    @Test
    void testPathUpdateStatusOrder_Success() throws Exception {
        UUID id = UUID.randomUUID();

        Order order = Order
                .builder()
                .id(id)
                .status(StatusOrder.CREATED)
                .items(List.of(Item
                        .builder()
                        .sku("prod-123")
                        .name("item-1")
                        .quantity(1)
                        .price(100.00)
                        .build()))
                .customer(Customer
                        .builder()
                        .name("cust-456")
                        .document("12345678901")
                        .build())
                .createdAt(LocalDateTime.now())
                .build();

        when(repository.findById(id)).thenReturn(Optional.of(order));

        mockMvc.perform(patch(BASE_URL + "/{id}/updateStatus/{status}", order.getId(), StatusOrder.COMPLETED.name())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testGetOrderById_Success() throws Exception {
        UUID id = UUID.randomUUID();

        Order order = Order
                .builder()
                .id(id)
                .status(StatusOrder.CREATED)
                .items(List.of(Item
                        .builder()
                        .sku("prod-123")
                        .name("item-1")
                        .price(100.00)
                        .build()))
                .customer(Customer
                        .builder()
                        .name("cust-456")
                        .document("12345678901")
                        .build())
                .createdAt(LocalDateTime.now())
                .build();

        when(repository.findById(id)).thenReturn(Optional.of(order));


        mockMvc.perform(get(BASE_URL+ "/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.customer.document").value("12345678901"))
                .andExpect(jsonPath("$.status").value(StatusOrder.CREATED.name()));
    }

    @Test
    void testGetOrderById_NotFound() throws Exception {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get(BASE_URL + "/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetOrdersByCustomerId_Success() throws Exception {
        UUID id = UUID.randomUUID();

        Order order = Order
                .builder()
                .id(id)
                .status(StatusOrder.CREATED)
                .items(List.of(Item
                        .builder()
                        .sku("prod-123")
                        .name("item-1")
                        .price(100.00)
                        .build()))
                .customer(Customer
                        .builder()
                        .name("cust-456")
                        .document("12345678901")
                        .build())
                .createdAt(LocalDateTime.now())
                .build();

        when(repository.findByCustomerDocument("12345678901", Pageable.ofSize(10))).thenReturn(new PageImpl<>(List.of(order)));

        mockMvc.perform(get(BASE_URL + "/customer/12345678901"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id").value(id.toString()))
                .andExpect(jsonPath("$.content[0].customer.document").value("12345678901"))
                .andExpect(jsonPath("$.content[0].status").value(StatusOrder.CREATED.name()));
    }

    @Test
    void testGetOrdersByCustomerId_NotFound() throws Exception {
        // Arrange
        when(repository.findByCustomerDocument("12345678901", Pageable.ofSize(10))).thenReturn(new PageImpl<>(List.of()));

        // Act & Assert
        mockMvc.perform(get(BASE_URL + "/customer/12345678901"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(0));
    }

    private String writeJsonToString(OrderDto json) throws JsonProcessingException {
        return objectMapper.writeValueAsString(json);
    }

}