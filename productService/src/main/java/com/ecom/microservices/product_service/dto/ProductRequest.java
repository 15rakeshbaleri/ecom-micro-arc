package com.ecom.microservices.product_service.dto;

public record ProductRequest(String name, String description, Integer price,
                             String skuCode) {
    // Constructor, getters, and other methods can be added if needed
}
