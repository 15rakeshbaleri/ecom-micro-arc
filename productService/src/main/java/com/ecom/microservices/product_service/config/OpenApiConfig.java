package com.ecom.microservices.product_service.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for customizing the OpenAPI/Swagger documentation
 * for the Product Service using SpringDoc.
 */
@Configuration
public class OpenApiConfig {

    /**
     * Defines a Spring Bean that provides custom OpenAPI metadata for Swagger UI.
     *
     * @return an OpenAPI instance with custom title, description, version, license, and external docs
     */
    @Bean
    public OpenAPI productServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        // Title of the API shown in Swagger UI
                        .title("Product Service API")

                        // Short description of the API
                        .description("API documentation for the Product Service")

                        // Version number of the API
                        .version("1.0.0")

                        // License information (appears in Swagger UI footer)
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")))

                // Link to external documentation (e.g., internal wiki, GitHub README, etc.)
                .externalDocs(new ExternalDocumentation()
                        .description("Product Service Wiki")
                        .url("https://example.com/product-service/docs"));
    }
}
