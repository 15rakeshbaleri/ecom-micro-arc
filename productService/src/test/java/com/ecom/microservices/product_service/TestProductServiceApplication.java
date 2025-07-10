package com.ecom.microservices.product_service;

import org.junit.jupiter.api.Disabled;
import org.springframework.boot.SpringApplication;

@Disabled("Disabled for now, as we are not running integration tests.")
public class TestProductServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(ProductServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
