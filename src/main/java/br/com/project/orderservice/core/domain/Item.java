package br.com.project.orderservice.core.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@Builder
@Document(collection = "items")
public class Item {

    @Id
    private UUID id;

    @Indexed(unique = true)
    private String sku;

    private String name;
    private String description;
    private Double price;
    private Integer quantity;

}
