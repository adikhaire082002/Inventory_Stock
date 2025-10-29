package com.aditya.inventory.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aditya.inventory.dto.BaseResponseDto;
import com.aditya.inventory.entity.TransactionalLog;
import com.aditya.inventory.service.ProductService;

@RestController
@RequestMapping("/Dealer")
public class DealerController {

	@Autowired
	ProductService productService;
	
	
	@PreAuthorize("hasRole('Dealer')")
	@PutMapping("/updateStock")
	public BaseResponseDto updateStock(@RequestParam Integer userID,Integer productId,int stockToUpdate){
		TransactionalLog updateStock = productService.updateStock(userID,productId,stockToUpdate);
	
		return new BaseResponseDto(HttpStatus.FOUND,"Stock updated Successfully by " + stockToUpdate,updateStock,new Date());
	}
}
