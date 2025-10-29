package com.aditya.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class InventoryStockApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryStockApplication.class, args);
	}

}
