package inventory_service;

import inventory_service.model.Inventory;
import inventory_service.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}


//	@Bean
//	public CommandLineRunner commandLineRunner(InventoryRepository inventoryRepository) {
//		return args -> {
//			inventoryRepository.save(new Inventory(null, "iphone_13", 100));
//			inventoryRepository.save(new Inventory(null, "iphone_14", 200));
//			inventoryRepository.save(new Inventory(null, "iphone_15", 300));
//			inventoryRepository.save(new Inventory(null, "iphone_16", 400));
//		};
//
//	}
}
