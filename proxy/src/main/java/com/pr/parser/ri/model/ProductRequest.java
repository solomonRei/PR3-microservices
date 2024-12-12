package com.pr.parser.ri.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    private String name;
    private String description;
    private String currency;
    private BigDecimal price;
    private ProductSpecificationRequest productSpecification;

    @Data
    public static class ProductSpecificationRequest {
        private String color;
        private String size;
        private String material;
    }
}

