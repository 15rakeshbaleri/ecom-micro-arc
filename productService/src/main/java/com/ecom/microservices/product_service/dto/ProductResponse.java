package com.ecom.microservices.product_service.dto;

public record ProductResponse(String skuCode, String name, String description, double price) {
}
