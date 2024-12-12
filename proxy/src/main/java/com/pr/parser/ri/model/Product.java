package com.pr.parser.ri.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private String name;
    private String price;
    private String link;
    private String currency;
    private Map<String, String> characteristics;

    public String toString() {
        return "Product(name=" + this.getName() + ", price=" + this.getPrice() + ", link=" + this.getLink() + ", currency=" + this.getCurrency() + ", characteristics=" + this.getCharacteristics() + ")";
    }
}
