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
 * Integration test class for Product Service using:
 * - Spring Boot test context
 * - Testcontainers (MongoDB)
 * - RestAssured for HTTP request testing
 */

@Import(TestcontainersConfiguration.class) // Custom config class for Testcontainers (if any)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // Boot app on random port for test isolation
class ProductServiceApplicationTests {

	/**
	 * Static MongoDBContainer to provide a running MongoDB instance for testing.
	 * The @ServiceConnection (Spring Boot 3.1+) integrates it with Spring Boot test config.
	 */
	@ServiceConnection
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.5");

	/**
	 * Injects the actual random port used by the application during the test.
	 * Used to configure RestAssured for correct target.
	 */
	@LocalServerPort
	private int port;

	/**
	 * This block runs once to start the MongoDB container before tests,
	 * and dynamically sets the `spring.data.mongodb.uri` property,
	 * so Spring Boot will connect to the containerized MongoDB instance.
	 */
	static {
		mongoDBContainer.start();
		System.setProperty("spring.data.mongodb.uri", mongoDBContainer.getReplicaSetUrl());
	}

	/**
	 * Configures RestAssured with the host and port of the Spring Boot application.
	 * This runs before each test method.
	 */
	@BeforeEach
	void setUp() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	/**
	 * Integration test for product creation API.
	 * Simulates a client sending a POST request to `/api/product/create`
	 * with product details, and validates the response structure and values.
	 */
	@Test
	void shouldCreateProduct() {
		// Sample JSON request to create a new product
		String requestBody = """
                {
                    "name": "iphone 15",
                    "description": "apple product",
                    "price": 60000
                }
                """;

		// Perform POST request using RestAssured and validate response
		RestAssured.given()
				.contentType("application/json")   // Set request content type
				.body(requestBody)                 // Attach JSON body
				.when()
				.post("/api/product/create")       // Endpoint under test
				.then()
				.statusCode(201)                   // Expect HTTP 201 Created
				.body("id", Matchers.notNullValue())                   // Product ID should not be null
				.body("name", Matchers.equalTo("iphone 15"))           // Validate name
				.body("description", Matchers.equalTo("apple product")) // Validate description
				.body("price", Matchers.equalTo(60000));               // Validate price value
	}
}
//we don't use postman because we used production db for testing
// but here we are using testcontainers to create a test db always new for testing