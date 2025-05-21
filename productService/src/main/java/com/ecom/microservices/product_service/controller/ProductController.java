package com.ecom.microservices.product_service.controller;

import com.ecom.microservices.product_service.dto.ProductRequest;
import com.ecom.microservices.product_service.dto.ProductResponse;
import com.ecom.microservices.product_service.model.Product;
import com.ecom.microservices.product_service.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@Slf4j
public class ProductController {

    @Autowired
    private ProductService productService;

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @PostMapping("/create")
    public ResponseEntity<Product> createProduct(@RequestBody ProductRequest productreq) {

        if (productreq == null) {
            logger.error("Product request is null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Received request to create product: {}", productreq);
       Product product = productService.createProduct(productreq);

        return new ResponseEntity<>(product,HttpStatus.CREATED);
    }
    @GetMapping("/getAll")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
//        try {
//            logger.info("Received request to get all products");
//            List<ProductResponse> productList = productService.getAllProducts();
//            if (productList.isEmpty()) {
//                logger.warn("No products found");
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            }
//            logger.info("Found {} products", productList.size());
//            Thread.sleep(5000);
//            return new ResponseEntity<>(productList, HttpStatus.OK);
//
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//
//        }
        logger.info("Received request to get all products");
        List<ProductResponse> productList = productService.getAllProducts();
        if (productList.isEmpty()) {
            logger.warn("No products found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        logger.info("Found {} products", productList.size());
        return new ResponseEntity<>(productList, HttpStatus.OK);

    }
}
