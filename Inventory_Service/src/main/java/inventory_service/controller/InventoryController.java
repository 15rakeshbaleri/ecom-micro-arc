package inventory_service.controller;

import inventory_service.DTO.BaseResponse;
import inventory_service.DTO.InventoryRequest;
import inventory_service.service.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/status/{skuCode}")
    public ResponseEntity<BaseResponse> getInventoryStatus(@PathVariable("skuCode") String skuCode,
                                                           @RequestParam("quantity") int quantity) {
        boolean quantity_check = inventoryService.checkInventory(skuCode, quantity);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("isInStock", quantity_check); // ✅ Use a meaningful name for a boolean field
        responseData.put("skuCode", skuCode);
        responseData.put("quantity", quantity);


        BaseResponse baseResponse = new BaseResponse();
        baseResponse.Status = "Success";
        baseResponse.messageType = "Inventory Status";
        baseResponse.message = "Inventory status retrieved successfully";
        baseResponse.timeStamp = new Date();
        baseResponse.data = responseData;

        return ResponseEntity.ok(baseResponse);
    }
    @PostMapping("/update")
    public ResponseEntity<BaseResponse> addOrUpdateInventory(@RequestBody InventoryRequest request) {
        boolean updated = inventoryService.addOrUpdateInventory(request.getSkuCode(), request.getQuantity());

        BaseResponse baseResponse = new BaseResponse();
        baseResponse.Status = updated ? "Success" : "Failure";
        baseResponse.messageType = "Inventory Update";
        baseResponse.message = updated ? "Inventory updated successfully" : "Failed to update inventory";
        baseResponse.timeStamp = new Date();

        return updated
                ? ResponseEntity.ok(baseResponse)
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
    }
}
