package br.com.project.orderservice.adapter.input;

import br.com.project.orderservice.core.domain.dto.OrderDto;
import br.com.project.orderservice.core.port.input.ManagerOrderServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderServiceListener {

    private final ManagerOrderServicePort orderService;

    @RabbitListener(queues = "create.order.queue")
    public void listenCreateOrderQueue(OrderDto order) {
        orderService.createOrder(order);
    }

}
