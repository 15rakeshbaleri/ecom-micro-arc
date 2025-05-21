package inventory_service.repository;

import inventory_service.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    public Inventory findBySkuCode(String skuCode);
}
