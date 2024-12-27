package br.com.project.orderservice.core.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@Builder
@Document(collection = "customers")
public class Customer {

    @Id
    private UUID id;
    private String document;
    private String name;
    private String email;
    private String phone;

}
