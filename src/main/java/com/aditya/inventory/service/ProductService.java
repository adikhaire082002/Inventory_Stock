package com.aditya.inventory.service;

import java.util.HashMap;
import java.util.List;

import com.aditya.inventory.dto.ProductDto;
import com.aditya.inventory.entity.Product;
import com.aditya.inventory.entity.TransactionalLog;

public interface ProductService {

	//ADD
	ProductDto addProduct(ProductDto productDto);
	
	//UPDATE PRODUCT
	ProductDto updateProduct(Integer id, ProductDto productDto);
	
	//UPDATE STOCKS
	TransactionalLog updateStock(Integer userID, Integer productId, int stockToUpdate);
	
	//FIND  ---NAME
	boolean existsByName(String name);
	
	//      ---ID
	ProductDto getProductById(Integer id);
	
	//GET
	HashMap<Integer, List<Product>> getProducts();
	 
	//      --BRAND NAME
	HashMap<Integer, List<Product>> getProductsbyBrand(String brandName);
	
	//      --CATEGORY
	HashMap<Integer, List<Product>> getProductsbyCategory(String categoryName);
	
	//      --IN RANGE
	HashMap<Integer, List<Product>> getProductsInRange(double from ,double to);
	
	//LOW STOCK
	List<Product> getProductsWithLowStock();
	
	//DELETE
	ProductDto deleteProduct(Integer id);
	
}
