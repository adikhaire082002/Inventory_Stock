package com.aditya.inventory.controller;

import java.util.Date;

import com.aditya.inventory.entity.Product;
import com.aditya.inventory.service.DealerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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
	public BaseResponseDto updateStock(@RequestParam Integer productId, int stockToUpdate, Authentication authentication) {
		TransactionalLog updateStock = productService.updateStock(authentication,productId,stockToUpdate);
		return new BaseResponseDto(HttpStatus.FOUND,"Stock updated Successfully by " + stockToUpdate,updateStock,new Date());
	}

    @PreAuthorize("hasRole('Dealer')")
    @GetMapping("/getStocks")
    public BaseResponseDto getStock(Authentication authentication,@RequestParam int page, @RequestParam int pageSize) {
        Page<Product> stock = dealerService.getStock(authentication, page, pageSize);
        return new BaseResponseDto(HttpStatus.FOUND,"All stocks fetched",stock,new Date());
    }

    @PreAuthorize("hasRole('Dealer')")
    @GetMapping("/LowStockAlert")
    public BaseResponseDto lowStockProducts(@RequestParam int page,int pageSize,Authentication authentication) {
        Page<Product> products = productService.getProductsWithLowStock(page,pageSize,authentication);
        return new BaseResponseDto(HttpStatus.FOUND,"Product below products having low stocks: ",products,new Date());
    }


}
