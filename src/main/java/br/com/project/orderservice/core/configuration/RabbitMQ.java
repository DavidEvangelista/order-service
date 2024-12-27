package br.com.project.orderservice.core.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQ {

    @Bean
    public Queue createOrderQueue() {
        return new Queue("create.order.queue", true);
    }

    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange("order.exchange");
    }

    @Bean
    public Binding binding(Queue createOrderQueue, DirectExchange orderExchange) {
        return BindingBuilder.bind(createOrderQueue).to(orderExchange).with("create-order.routing.key");
    }

}
