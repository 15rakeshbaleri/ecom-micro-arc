package inventory_service.service;

import inventory_service.model.Inventory;
import inventory_service.repository.InventoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Transactional // NOT readOnly
    public boolean checkInventory(String skuCode, int quantity) {
        Inventory inventory = inventoryRepository.findBySkuCode(skuCode);
        if (inventory != null) {
            int availableQuantity = inventory.getQuantity();
            if (availableQuantity >= quantity) {
                inventory.setQuantity(availableQuantity - quantity);
                inventoryRepository.save(inventory);
                log.info("Inventory check for SKU {}: Available quantity is sufficient", skuCode);
                return true;
            } else {
                log.warn("Inventory check for SKU {}: Insufficient quantity", skuCode);
                return false;
            }
        }
        log.warn("Inventory check for SKU {}: SKU not found", skuCode);
        return false;
    }


}
