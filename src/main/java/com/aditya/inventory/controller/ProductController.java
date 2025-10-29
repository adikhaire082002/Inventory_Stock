package com.aditya.inventory.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aditya.inventory.dto.BaseResponse;
import com.aditya.inventory.dto.BaseResponseDto;
import com.aditya.inventory.dto.ProductDto;
import com.aditya.inventory.entity.Product;
import com.aditya.inventory.service.ProductService;
import com.aditya.inventory.service.TransactionService;

@RestController
@RequestMapping("/Product")
public class ProductController {
	
	
	@Autowired
	ProductService productService;
	
	@Autowired
	TransactionService transactionService;
	
	
	//Admin Access Only
	
	@PreAuthorize("hasRole('Admin')") 
	@PostMapping("/Addproduct-Admin")
	public BaseResponse addProduct(@RequestBody ProductDto productDto){
		String productName = productDto.getName();
		
		if(productService.existsByName(productName)) {
			return new BaseResponse(HttpStatus.FOUND,"Product Already Added "+ productDto.getName() +" with this name",new Date());
		}
		productService.addProduct(productDto);
		return new BaseResponse(HttpStatus.CREATED,"Product Added successfully ",new Date());
		
	}
	
	@PreAuthorize("hasRole('Admin')")
	@PutMapping("/UpdateProduct-Admin")
	public BaseResponseDto updateProduct(@RequestParam Integer id,@RequestBody ProductDto productDto){
		ProductDto updatedProduct = productService.updateProduct(id,productDto);
		
		return new BaseResponseDto(HttpStatus.RESET_CONTENT,"Product updated successfully ",updatedProduct,new Date());
		
		
	}
	
	@PreAuthorize("hasRole('Admin')")
	@DeleteMapping("/DeleteProduct-Admin")
	public BaseResponseDto deleteProduct(@RequestParam Integer id){
		ProductDto deletedProduct = productService.deleteProduct(id);
	
		return new BaseResponseDto(HttpStatus.GONE,"Product Deleted successfully ",deletedProduct,new Date());
		
		
	}


	
	//All User Access
	
	
	@PreAuthorize("hasAnyRole('dDaler','Customer','Admin')")
	@GetMapping("/ViewProduct")
	public BaseResponseDto getProduct(@RequestParam Integer id){
		ProductDto productById = productService.getProductById(id);
		
		return new BaseResponseDto(HttpStatus.FOUND,"Product found ",productById,new Date());
		
	}
	
	@PreAuthorize("hasAnyRole('Dealer','Customer','Admin')")
	@GetMapping("/ViewProducts")
	public BaseResponseDto getProducts(){
		HashMap<Integer, List<Product>> products = productService.getProducts();
		if(products.size() < 1 ) {
			return new BaseResponseDto(HttpStatus.NOT_FOUND,"Not any product added yet",null,new Date());
		}
		return new BaseResponseDto(HttpStatus.FOUND,"All products fectched",products,new Date());
	}
	
	@PreAuthorize("hasAnyRole('Dealer','Customer','Admin')")
	@GetMapping("/ViewProducts/Brands")
	public BaseResponseDto getProductsByBrand(@RequestParam String brandName){
		HashMap<Integer, List<Product>> products = productService.getProductsbyBrand(brandName);
		if(products.size() < 1 ) {
			return new BaseResponseDto(HttpStatus.NOT_FOUND,"Not any product added with "+brandName + " this brand",null,new Date());
		}
		return new BaseResponseDto(HttpStatus.FOUND,"All products fectched with "+brandName + " this brand",products,new Date());
	}
	
	@PreAuthorize("hasAnyRole('Dealer','Customer','Admin')")
	@GetMapping("/ViewProducts/Category")
	public BaseResponseDto getProductsByCategory(@RequestParam String categoryName){
		HashMap<Integer, List<Product>> products = productService.getProductsbyCategory(categoryName);
		if(products.size() < 1 ) {
			return new BaseResponseDto(HttpStatus.NOT_FOUND,"Not any product added with "+categoryName + " this category",null,new Date());
		}
		return new BaseResponseDto(HttpStatus.FOUND,"All products fectched with "+categoryName + " this category",products,new Date());
	}
	
	
	@PreAuthorize("hasAnyRole('Dealer','Customer','Admin')")
	@GetMapping("/ViewProducts/PriceBeetween")
	public BaseResponseDto getProductsByCategory(@RequestParam double from , double to){
		HashMap<Integer, List<Product>> products = productService.getProductsInRange(from,to);
		if(products.size() < 1 ) {
			return new BaseResponseDto(HttpStatus.NOT_FOUND,"Not any product found in range between "+from+" to " + to,null,new Date());
		}
		return new BaseResponseDto(HttpStatus.FOUND,"All products fectched between price range "+from+" to " + to,products,new Date());
	}
	
	
	
	

}
