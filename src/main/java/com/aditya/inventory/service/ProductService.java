package com.aditya.inventory.service;

import java.util.HashMap;
import java.util.List;

import com.aditya.inventory.dto.ProductDto;
import com.aditya.inventory.entity.Product;
import com.aditya.inventory.entity.TransactionalLog;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

public interface ProductService {

	//ADD
	ProductDto addProduct(ProductDto productDto,Authentication authentication);
	
	//UPDATE PRODUCT
	ProductDto updateProduct(Integer id, ProductDto productDto, Authentication authentication);
	
	//UPDATE STOCKS
	TransactionalLog updateStock(Authentication authentication, Integer productId, int stockToUpdate);
	
	//FIND  ---NAME
	void existsByName(String name);
	
	//      ---ID
	ProductDto getProductById(Integer id);
	
	//GET
	Page<Product> getProducts(int page, int pageSize);
	 
	//      --BRAND NAME
    Page<Product> getProductsbyBrand(String brandName,int page, int pageSize);
	
	//      --CATEGORY
    Page<Product> getProductsbyCategory(String categoryName,int page, int pageSize);
	
	//      --IN RANGE
    Page<Product> getProductsInRange(double from ,double to,int page, int pageSize);
	
	//LOW STOCK
    Page<Product> getProductsWithLowStock(int page, int pageSize,Authentication authentication);

    Page<Product> getProductsByName(String name, int page, int pageSize);
	
	//DELETE
	boolean deleteProduct(Integer id,Authentication authentication);
	
}
