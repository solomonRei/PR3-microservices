package com.pr.parser.ri;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pr.parser.config.RabbitMQConfig;
import com.pr.parser.ri.model.FilteredProductsResult;
import com.pr.parser.ri.model.Product;
import com.pr.parser.ri.model.ProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ListenerSend {


    private final ObjectMapper objectMapper;
    private final WebClient webClient;

    @RabbitListener(queues = RabbitMQConfig.FILTERED_PRODUCTS_QUEUE)
    public void handleMessageConsumer1(String message) {
        try {
            FilteredProductsResult result = objectMapper.readValue(message, FilteredProductsResult.class);

            result.getFilteredProducts().forEach(product -> {
                ProductRequest request = mapToProductRequest(product);

                sendProductToLab2(request);
            });

        } catch (Exception e) {
            System.err.println("Error processing message in Consumer 1: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private ProductRequest mapToProductRequest(Product product) {
        Map<String, String> characteristics = product.getCharacteristics();

        String color = characteristics.getOrDefault("Цвет", "Unknown");
        String size = characteristics.getOrDefault("Диагональ дисплея", "Unknown");
        String material = characteristics.getOrDefault("Материал корпуса", "Unknown");

        var specification = new ProductRequest.ProductSpecificationRequest();
        specification.setColor(color);
        specification.setSize(size);
        specification.setMaterial(material);

        return new ProductRequest(
                product.getName(),
                "Automatically imported product",
                product.getCurrency().toString(),
                new BigDecimal(product.getPrice()),
                specification
        );
    }

    private void sendProductToLab2(ProductRequest productRequest) {
        webClient.post()
                .uri("http://http-server-1:8082/product/save")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData("productRequest", productRequest))
                .retrieve()
                .bodyToMono(Void.class)
                .doOnSuccess(response -> System.out.println("Successfully sent product to Lab2: " + productRequest.getName()))
                .doOnError(error -> System.err.println("Failed to send product to Lab2: " + error.getMessage()))
                .block();
    }
}
