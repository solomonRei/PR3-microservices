package com.pr.parser.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String PRODUCTS_EXCHANGE = "products_exchange";
    public static final String FILTERED_PRODUCTS_QUEUE = "filtered_products_queue";
    public static final String FILTERED_PRODUCTS_ROUTING_KEY = "filtered_products";

    @Bean
    public DirectExchange productsExchange() {
        return new DirectExchange(PRODUCTS_EXCHANGE, true, false);
    }

    @Bean
    public Queue filteredProductsQueue() {
        return QueueBuilder.durable(FILTERED_PRODUCTS_QUEUE).build();
    }

    @Bean
    public Binding bindFilteredProductsQueue(@Qualifier("filteredProductsQueue") Queue filteredProductsQueue,
                                             @Qualifier("productsExchange") DirectExchange productsExchange) {
        return BindingBuilder.bind(filteredProductsQueue).to(productsExchange).with(FILTERED_PRODUCTS_ROUTING_KEY);
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("trx-events-directs", true, false);
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange("dlx-exchange", true, false);
    }

    @Bean
    public Queue quorumQueue() {
        return QueueBuilder.durable("eos-queue")
                .withArgument("x-queue-type", "quorum")
                .withArgument("x-dead-letter-exchange", "dlx-exchange")
                .withArgument("x-dead-letter-routing-key", "dlx-routing-key")
                .build();
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable("dlx-queue").build();
    }

    @Bean
    public Binding binding(@Qualifier("quorumQueue") Queue quorumQueue,
                           @Qualifier("directExchange") DirectExchange directExchange) {
        return BindingBuilder.bind(quorumQueue).to(directExchange).with("my_stream_key");
    }


    @Bean
    public Binding deadLetterBinding(@Qualifier("deadLetterQueue") Queue deadLetterQueue,
                                     @Qualifier("deadLetterExchange") DirectExchange deadLetterExchange) {
        return BindingBuilder.bind(deadLetterQueue).to(deadLetterExchange).with("dlx-routing-key");
    }

}
