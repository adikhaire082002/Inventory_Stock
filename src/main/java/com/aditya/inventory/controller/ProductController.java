package com.aditya.inventory.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

	//Admin Access Only
	
	@PreAuthorize("hasRole('Dealer')")
	@PostMapping("/addProduct")
	public ResponseEntity<BaseResponse> addProduct(@RequestBody ProductDto productDto, Authentication authentication) throws IOException {
		productService.addProduct(productDto,authentication);
        BaseResponse response = new BaseResponse(HttpStatus.OK,"Product Added successfully ",new Date());
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize("hasRole('Admin')")
	@PatchMapping("/updateProduct")
	public ResponseEntity<BaseResponseDto> updateProduct(@RequestParam Integer id, @RequestBody ProductDto productDto, Authentication authentication){
		ProductDto updatedProduct = productService.updateProduct(id,productDto,authentication);
        BaseResponseDto response = new BaseResponseDto(HttpStatus.OK,"Product updated successfully ",updatedProduct,new Date());
		return ResponseEntity.ok(response);
		
	}
	
	@PreAuthorize("hasRole('Admin')")
	@DeleteMapping("/deleteProduct")
	public ResponseEntity<BaseResponse> deleteProduct(@RequestParam Integer id,Authentication authentication){
		productService.deleteProduct(id,authentication);
        BaseResponse response = new BaseResponse(HttpStatus.OK,"Product Deleted successfully ",new Date());
		return ResponseEntity.ok(response);
		
	}
	
	//All User Access
	@PreAuthorize("hasAnyRole('Dealer','Customer','Admin')")
	@GetMapping("/viewProduct")
	public ResponseEntity<BaseResponseDto> getProduct(@RequestParam Integer id) throws FileNotFoundException {
		ProductDto productById = productService.getProductById(id);
        BaseResponseDto response = new BaseResponseDto(HttpStatus.OK,"Product found ",productById,new Date());
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize("hasAnyRole('Dealer','Customer','Admin')")
	@GetMapping("/viewProducts")
	public ResponseEntity<BaseResponseDto> getProducts(@RequestParam(defaultValue = "1") Integer page , @RequestParam Integer pageSize) throws FileNotFoundException {
		Page<ProductDto> products = productService.getProducts(page,pageSize);
        BaseResponseDto response = new BaseResponseDto(HttpStatus.OK,"All products fetched",products,new Date());
	    return ResponseEntity.ok(response);
    }

	
	@PreAuthorize("hasAnyRole('Dealer','Customer','Admin')")
	@GetMapping("/viewProducts/Brands")
	public ResponseEntity<BaseResponseDto> getProductsByBrand(@RequestParam String brandName,@RequestParam(defaultValue = "1") Integer page , @RequestParam Integer pageSize) throws FileNotFoundException {
        Page<ProductDto> products = productService.getProductsbyBrand(brandName,page,pageSize);
        BaseResponseDto response = new BaseResponseDto(HttpStatus.OK,"All products fetched with "+brandName + " this brand",products,new Date());
	    return ResponseEntity.ok(response);
    }
	
	@PreAuthorize("hasAnyRole('Dealer','Customer','Admin')")
	@GetMapping("/viewProducts/Category")
	public ResponseEntity<BaseResponseDto> getProductsByCategory(@RequestParam String categoryName,@RequestParam(defaultValue = "1") Integer page , @RequestParam Integer pageSize) throws FileNotFoundException {
        Page<ProductDto> products = productService.getProductsbyCategory(categoryName,page,pageSize);
		BaseResponseDto response =  new BaseResponseDto(HttpStatus.OK,"All products fetched with "+categoryName + " this category",products,new Date());
	    return ResponseEntity.ok(response);
    }


	@PreAuthorize("hasAnyRole('Dealer','Customer','Admin')")
	@GetMapping("/viewProducts/priceBetween")
	public ResponseEntity<BaseResponseDto> getProductsByCategory(@RequestParam double from , double to,@RequestParam(defaultValue = "1") Integer page , @RequestParam Integer pageSize) throws FileNotFoundException {
        Page<ProductDto> products = productService.getProductsInRange(from,to,page,pageSize);
        BaseResponseDto response = new BaseResponseDto(HttpStatus.OK,"All products fetched between price range "+from+" to " + to,products,new Date());
        return ResponseEntity.ok(response);
    }

}
