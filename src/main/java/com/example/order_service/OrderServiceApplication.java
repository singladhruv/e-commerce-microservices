package com.example.order_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class OrderServiceApplication {

	public static void main(String[] args) {
		// Explicitly set port if not in properties
		System.setProperty("server.port", "8081");
		ConfigurableApplicationContext context = SpringApplication.run(OrderServiceApplication.class, args);
		System.out.println("Server running on port: " + 
			((ServletWebServerApplicationContext) context).getWebServer().getPort());
	}
}
