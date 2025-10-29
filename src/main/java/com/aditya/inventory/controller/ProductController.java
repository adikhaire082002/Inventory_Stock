package com.aditya.inventory.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.aditya.inventory.dto.BaseResponse;
import com.aditya.inventory.dto.BaseResponseDto;
import com.aditya.inventory.dto.ProductDto;
import com.aditya.inventory.entity.Product;
import com.aditya.inventory.service.ProductService;
import com.aditya.inventory.service.TransactionService;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/Product")
public class ProductController {
	
	
	@Autowired
	ProductService productService;
	
	@Autowired
	TransactionService transactionService;
	
	
	//Admin Access Only
	
	@PreAuthorize("hasRole('Dealer')")
	@PostMapping("/addProduct")
	public BaseResponse addProduct(@RequestBody ProductDto productDto, Authentication authentication) throws IOException {
		productService.addProduct(productDto,authentication);
		return new BaseResponse(HttpStatus.CREATED,"Product Added successfully ",new Date());
		
	}
	
	@PreAuthorize("hasRole('Admin')")
	@PatchMapping("/updateProduct")
	public BaseResponseDto updateProduct(@RequestParam Integer id, @RequestBody ProductDto productDto, Authentication authentication){
		ProductDto updatedProduct = productService.updateProduct(id,productDto,authentication);
		return new BaseResponseDto(HttpStatus.RESET_CONTENT,"Product updated successfully ",updatedProduct,new Date());
		
		
	}
	
	@PreAuthorize("hasRole('Admin')")
	@DeleteMapping("/deleteProduct")
	public BaseResponse deleteProduct(@RequestParam Integer id,Authentication authentication){
		productService.deleteProduct(id,authentication);
		return new BaseResponse(HttpStatus.GONE,"Product Deleted successfully ",new Date());
		
		
	}
	
	//All User Access
	@PreAuthorize("hasAnyRole('Dealer','Customer','Admin')")
	@GetMapping("/viewProduct")
	public BaseResponseDto getProduct(@RequestParam Integer id){
		Product productById = productService.getProductById(id);
		return new BaseResponseDto(HttpStatus.FOUND,"Product found ",productById,new Date());
		
	}
	
	@PreAuthorize("hasAnyRole('Dealer','Customer','Admin')")
	@GetMapping("/viewProducts")
	public BaseResponseDto getProducts(@RequestParam(defaultValue = "1") Integer page , @RequestParam Integer pageSize){
		Page<Product> products = productService.getProducts(page,pageSize);
		return new BaseResponseDto(HttpStatus.FOUND,"All products fectched",products,new Date());
	}
	
	@PreAuthorize("hasAnyRole('Dealer','Customer','Admin')")
	@GetMapping("/viewProducts/Brands")
	public BaseResponseDto getProductsByBrand(@RequestParam String brandName,@RequestParam(defaultValue = "1") Integer page , @RequestParam Integer pageSize){
        Page<Product> products = productService.getProductsbyBrand(brandName,page,pageSize);
		return new BaseResponseDto(HttpStatus.FOUND,"All products fectched with "+brandName + " this brand",products,new Date());
	}
	
	@PreAuthorize("hasAnyRole('Dealer','Customer','Admin')")
	@GetMapping("/viewProducts/Category")
	public BaseResponseDto getProductsByCategory(@RequestParam String categoryName,@RequestParam(defaultValue = "1") Integer page , @RequestParam Integer pageSize){
        Page<Product> products = productService.getProductsbyCategory(categoryName,page,pageSize);
		return new BaseResponseDto(HttpStatus.FOUND,"All products fectched with "+categoryName + " this category",products,new Date());
	}


	@PreAuthorize("hasAnyRole('Dealer','Customer','Admin')")
	@GetMapping("/viewProducts/priceBetween")
	public BaseResponseDto getProductsByCategory(@RequestParam double from , double to,@RequestParam(defaultValue = "1") Integer page , @RequestParam Integer pageSize){
        Page<Product> products = productService.getProductsInRange(from,to,page,pageSize);
		return new BaseResponseDto(HttpStatus.FOUND,"All products fectched between price range "+from+" to " + to,products,new Date());
	}

}
