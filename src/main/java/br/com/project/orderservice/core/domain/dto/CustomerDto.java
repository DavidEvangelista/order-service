package br.com.project.orderservice.core.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerDto {

    private String id;
    @NotNull
    private String document;
    private String name;
    private String email;
    private String phone;

}
