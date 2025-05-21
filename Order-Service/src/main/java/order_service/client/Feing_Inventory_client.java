package order_service.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import order_service.dto.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "inventory-service", url = "${inventory.url}")
public interface Feing_Inventory_client {

    @GetMapping("/api/inventory/status/{skuCode}")
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
    @Retry(name = "inventory")
    ResponseEntity<BaseResponse> getInventoryStatus(@PathVariable("skuCode") String skuCode,
                                                    @RequestParam("quantity") int quantity);

    // Must match method signature exactly (skuCode, quantity, Throwable)
    default ResponseEntity<BaseResponse> fallbackMethod(String skuCode, int quantity, Throwable throwable) {
        BaseResponse response = new BaseResponse();
        response.setMessage("Inventory service is currently unavailable. Please try again later.");
        response.setStatus("FAILURE");
        return ResponseEntity.status(503).body(response);
    }
}
