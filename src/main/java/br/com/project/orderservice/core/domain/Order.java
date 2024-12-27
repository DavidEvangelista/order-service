package br.com.project.orderservice.core.domain;

import br.com.project.orderservice.core.domain.enums.StatusOrder;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@Document(collection = "orders")
public class Order {

    @Id
    private UUID id;
    private List<Item> items;

    @Indexed
    private Customer customer;

    @Indexed
    private StatusOrder status;
    private Double total;

    @Indexed
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
