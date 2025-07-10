package com.ecom.microservices.product_service.dto;

import lombok.Data;

@Data
public class InventoryRequest {
    private String skuCode;
    private int quantity;
}