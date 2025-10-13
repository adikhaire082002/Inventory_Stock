package com.aditya.inventory.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

	@Bean
	OpenAPI customOpenAPI() {
		
		return new OpenAPI()
				.info(new Info().title("JavaInUse Authentication Service"))                                    // Set the title of your API that appears in Swagger UI
				.addSecurityItem(new SecurityRequirement().addList("JavaInUseSecurityScheme"))                 // Tell Swagger that the API uses a security scheme (JWT token)
				.components(new Components().addSecuritySchemes("JavaInUseSecurityScheme",                     // Define the security scheme details
						new SecurityScheme()                                                                   // Name of the security scheme
						.name("JavaInUseSecurityScheme")
						.type(SecurityScheme.Type.HTTP)                                                        // Type of auth: HTTP
						.scheme("bearer").bearerFormat("JWT")));                                               // Use Bearer token (JWT) for authentication
		
	}
}
