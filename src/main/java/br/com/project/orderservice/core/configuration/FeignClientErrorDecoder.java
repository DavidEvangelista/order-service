package br.com.project.orderservice.core.configuration;

import br.com.project.orderservice.core.exceptions.FeignClientBadRequestException;
import br.com.project.orderservice.core.exceptions.FeignClientInternalServerErrorException;
import br.com.project.orderservice.core.exceptions.FeignClientNotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class FeignClientErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder errorDecoder = new Default();
    @Override
    public Exception decode(String s, Response response) {
        return switch (response.status()) {
            case 400 -> new FeignClientBadRequestException(response.reason());
            case 404 -> new FeignClientNotFoundException(response.reason());
            case 500 -> new FeignClientInternalServerErrorException(response.reason());
            default -> errorDecoder.decode(s, response);
        };
    }
}
