package com.pr.parser.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pr.parser.model.FilteredProductsResult;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


@Component
@AllArgsConstructor
public class PhonePublisher {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public Mono<Void> publishFilteredProductsResult(FilteredProductsResult filteredProductsResult) {
        return Mono.fromRunnable(() -> {
            try {
                String message = objectMapper.writeValueAsString(filteredProductsResult);
                rabbitTemplate.convertAndSend("products_exchange", "filtered_products", message);
                System.out.println("Published to products_exchange: " + message);
            } catch (Exception e) {
                throw new RuntimeException("Failed to publish message", e);
            }
        });
    }
}

