package com.aditya.inventory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.aditya.inventory.entity.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer> {

	boolean existsByName(String name);

	@Query("Select p from Product p")
	List<Product> getProducts();
	
	@Query("Select p from Product p where p.quantity < 10")
	List<Product> getLowStockProducts();

	List<Product> findByBrand(String brandName);

	List<Product> findByCategories_Name(String categoryName);

	List<Product> findByPriceBetween(double from, double to);

	

}
