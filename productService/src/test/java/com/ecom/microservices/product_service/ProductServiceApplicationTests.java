package com.ecom.microservices.product_service;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MongoDBContainer;
import static org.hamcrest.Matchers.*;


/**
 * Integration test class for Product Service using Spring Boot, Testcontainers, and RestAssured.
 */
@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // Start app on a random port
class ProductServiceApplicationTests {

	// Static MongoDB container to spin up a temporary MongoDB instance for testing
	@ServiceConnection
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.5");

	@LocalServerPort // Injects the random port the application is running on
	private int port;

	/**
	 * This method sets the base URI and port for RestAssured before each test runs.
	 */
	@BeforeEach
	void setUp() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	// Static block to start the MongoDB container and set the Spring Boot MongoDB URI property
	static {
		mongoDBContainer.start();
		System.setProperty("spring.data.mongodb.uri", mongoDBContainer.getReplicaSetUrl());
	}

	/**
	 * Integration test to verify product creation endpoint.
	 * It sends a POST request to /api/product/create and validates the response.
	 */
	@Test
	void shouldCreateProduct() {
		// JSON request body to create a new product
		String requestBody = """
                {
                    "name":"iphone 15",
                    "description":"apple product",
                    "price":60000
                }
                """;

		// Send POST request and assert the response
		RestAssured.given()
				.contentType("application/json") // Set request content type to JSON
				.body(requestBody)               // Attach request body
				.when()
				.post("/api/product/create")     // Endpoint being tested
				.then()
				.statusCode(201)                 // Expect HTTP 201 Created
				.body("id", Matchers.notNullValue())                  // Validate ID is returned
				.body("name", Matchers.equalTo("iphone 15"))          // Validate name field
				.body("description", Matchers.equalTo("apple product")) // Validate description
				.body("price", Matchers.equalTo(60000));            // Validate price as double
	}
}
