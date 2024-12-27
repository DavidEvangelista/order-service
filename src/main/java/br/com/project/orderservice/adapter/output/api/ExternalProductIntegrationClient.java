package br.com.project.orderservice.adapter.output.api;

import br.com.project.orderservice.core.configuration.FeignClientErrorDecoder;
import br.com.project.orderservice.core.domain.dto.OrderDto;
import br.com.project.orderservice.core.port.output.api.ExternalProductBServicePort;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "ExternalProductIntegrationClient",
        url = "${services.external-product-integration.url}",
        configuration = FeignClientErrorDecoder.class)
public interface ExternalProductIntegrationClient extends ExternalProductBServicePort {
    @Override
    @RequestMapping(method = RequestMethod.POST, value = "/orders")
    void createOrder(@RequestBody OrderDto order);

}
