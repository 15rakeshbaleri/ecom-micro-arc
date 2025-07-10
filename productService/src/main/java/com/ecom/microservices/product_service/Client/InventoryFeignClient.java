package com.ecom.microservices.product_service.Client;

import com.ecom.microservices.product_service.dto.InventoryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "inventory-service", url = "${inventory.url}")
public interface InventoryFeignClient {

    @PostMapping("/api/inventory/update")
    ResponseEntity<String> addOrUpdateInventory(@RequestBody InventoryRequest request);


}
