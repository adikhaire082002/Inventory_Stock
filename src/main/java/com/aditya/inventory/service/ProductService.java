package com.aditya.inventory.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import com.aditya.inventory.dto.ProductDto;
import com.aditya.inventory.entity.Product;
import com.aditya.inventory.entity.TransactionalLog;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {

	//ADD
	ProductDto addProduct(ProductDto productDto, Authentication authentication) throws IOException;
	
	//UPDATE PRODUCT
	ProductDto updateProduct(Integer id, ProductDto productDto, Authentication authentication);
	
	//UPDATE STOCKS
	TransactionalLog updateStock(Authentication authentication, Integer productId, int stockToUpdate);
	
	//FIND  ---NAME
	void existsByName(String name);
	
	//      ---ID
	ProductDto getProductById(Integer id) throws FileNotFoundException;
	
	//GET
	Page<ProductDto> getProducts(int page, int pageSize) throws FileNotFoundException;
	 
	//      --BRAND NAME
    Page<ProductDto> getProductsbyBrand(String brandName,int page, int pageSize) throws FileNotFoundException;
	
	//      --CATEGORY
    Page<ProductDto> getProductsbyCategory(String categoryName,int page, int pageSize) throws FileNotFoundException;
	
	//      --IN RANGE
    Page<ProductDto> getProductsInRange(double from ,double to,int page, int pageSize) throws FileNotFoundException;
	
	//LOW STOCK
    Page<ProductDto> getProductsWithLowStock(int page, int pageSize,Authentication authentication) throws FileNotFoundException;

    Page<ProductDto> getProductsByName(String name, int page, int pageSize) throws FileNotFoundException;
	
	//DELETE
	boolean deleteProduct(Integer id,Authentication authentication) throws IOException;
	
}
