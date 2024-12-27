package br.com.project.orderservice.core.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemDto {

    private String id;

    @NotBlank
    private String sku;

    private String name;
    private String description;

    @NotNull
    private Double price;

    @NotNull
    @Max(Integer.MAX_VALUE)
    @Min(1)
    private Integer quantity;

    @JsonIgnore
    public Double getAmount() {
        return quantity * price;
    }

}
