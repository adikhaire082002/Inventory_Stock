package com.aditya.inventory.controller;

import java.io.FileNotFoundException;
import java.util.Date;

import com.aditya.inventory.dto.ProductDto;
import com.aditya.inventory.entity.Product;
import com.aditya.inventory.service.DealerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.aditya.inventory.dto.BaseResponseDto;
import com.aditya.inventory.entity.TransactionalLog;
import com.aditya.inventory.service.ProductService;

@RestController
@RequestMapping("/Dealer")
public class DealerController {

	@Autowired
	ProductService productService;

    @Autowired
    DealerService dealerService;
	
	
	@PreAuthorize("hasRole('Dealer')")
	@PatchMapping("/updateStock")
	public ResponseEntity<BaseResponseDto> updateStock(@RequestParam Integer productId, int stockToUpdate, Authentication authentication) {
		TransactionalLog updateStock = productService.updateStock(authentication,productId,stockToUpdate);
		BaseResponseDto response = new BaseResponseDto(HttpStatus.FOUND,"Stock updated Successfully by " + stockToUpdate,updateStock,new Date());
	    return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('Dealer')")
    @GetMapping("/getStocks")
    public ResponseEntity<BaseResponseDto> getStock(Authentication authentication,@RequestParam int page, @RequestParam int pageSize) throws FileNotFoundException {
        Page<ProductDto> stock = dealerService.getStock(authentication, page, pageSize);
        BaseResponseDto response = new BaseResponseDto(HttpStatus.FOUND,"All stocks fetched",stock,new Date());
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('Dealer')")
    @GetMapping("/LowStockAlert")
    public ResponseEntity<BaseResponseDto> lowStockProducts(@RequestParam int page,int pageSize,Authentication authentication) throws FileNotFoundException {
        Page<ProductDto> products = productService.getProductsWithLowStock(page,pageSize,authentication);
        BaseResponseDto response = new BaseResponseDto(HttpStatus.FOUND,"Below products having low stocks: ",products,new Date());
        return ResponseEntity.ok(response);
    }


}
