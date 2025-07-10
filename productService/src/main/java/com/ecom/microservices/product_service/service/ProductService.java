package com.ecom.microservices.product_service.service;

import com.ecom.microservices.product_service.Client.InventoryFeignClient;
import com.ecom.microservices.product_service.dto.InventoryRequest;
import com.ecom.microservices.product_service.dto.ProductRequest;
import com.ecom.microservices.product_service.dto.ProductResponse;
import com.ecom.microservices.product_service.model.Product;
import com.ecom.microservices.product_service.repo.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProductService {


    @Autowired
    private InventoryFeignClient inventoryFeignClient;
    @Autowired
    private  ProductRepository productRepository;
    public Product createProduct(ProductRequest productreq) {
        Product product = new Product();
        product.setSkuCode(productreq.skuCode());
        product.setName(productreq.name());
        product.setDescription(productreq.description());
        product.setPrice(productreq.price());
        InventoryRequest inventoryRequest = new InventoryRequest();
        inventoryRequest.setSkuCode(productreq.skuCode());
        inventoryFeignClient.addOrUpdateInventory(inventoryRequest);
        log.info("Product created successfully: {}", product);
     return    productRepository.save(product);
    }


    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        log.info("Products fetched successfully: {}", products);
        return products.stream()
                .map(product -> new ProductResponse(product.getSkuCode(), product.getName(), product.getDescription(), product.getPrice()))
                .toList();
    }
}
