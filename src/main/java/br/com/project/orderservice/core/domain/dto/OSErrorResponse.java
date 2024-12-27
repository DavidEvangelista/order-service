package br.com.project.orderservice.core.domain.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OSErrorResponse {

    @NotNull
    private String message;

    @NotNull
    private String path;

    @Min(0)
    private Integer statusCode;

    @NotNull
    private String statusMessage;



}
