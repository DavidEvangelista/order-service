package br.com.project.orderservice.core.domain.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@ToString
public class OrderDto {

    private String id;

    @NotEmpty
    @Valid
    private List<ItemDto> items;

    @Valid
    private CustomerDto customer;
    private Double total;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String status;

}
