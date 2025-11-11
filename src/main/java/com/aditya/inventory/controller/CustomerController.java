package com.aditya.inventory.controller;


import com.aditya.inventory.dto.BaseResponseDto;
import com.aditya.inventory.entity.Cart;
import com.aditya.inventory.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/Customer")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @PreAuthorize("hasRole('Customer')")
    @PostMapping("/addToCart")
    public ResponseEntity<BaseResponseDto> addProductToCart(@RequestParam Integer product_id, Authentication authentication) {
        Cart cart = customerService.addToCart(product_id, authentication);
        BaseResponseDto response = new BaseResponseDto(HttpStatus.CREATED, "Product Added to cart", cart, new Date());
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('Customer')")
    @PostMapping("/addToCartWithQuantity")
    public ResponseEntity<BaseResponseDto> addProductToCart(@RequestParam Integer product_id,@RequestParam Integer quantity, Authentication authentication) {
        Cart cart = customerService.addToCart(product_id, authentication,quantity);
        BaseResponseDto response =new BaseResponseDto(HttpStatus.CREATED,"Product Added to cart",cart,new Date());
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('Customer')")
    @PatchMapping("/increaseProductQuantity")
    public ResponseEntity<BaseResponseDto>  increaseQuantityInCart(@RequestParam Integer product_id, Authentication authentication) {
        Cart cart = customerService.addToCart(product_id, authentication);
        BaseResponseDto response = new BaseResponseDto(HttpStatus.CREATED,"Product Quantity increase In cart",cart,new Date());
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('Customer')")
    @DeleteMapping("/removeFromCart")
    public ResponseEntity<BaseResponseDto>  removeFromCart(@RequestParam Integer product_id, Authentication authentication) {
        Cart cart = customerService.removeProduct(authentication,product_id);
        BaseResponseDto response = new BaseResponseDto(HttpStatus.CREATED,"Product removed from cart",cart,new Date());
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('Customer')")
    @PatchMapping("/decreaseProductQuantity")
    public ResponseEntity<BaseResponseDto> decreaseQuantityInCart(@RequestParam Integer product_id, Authentication authentication) {
        Cart cart = customerService.decreaseQuantity(authentication,product_id);
        BaseResponseDto response = new BaseResponseDto(HttpStatus.CREATED,"Product Quantity decrease In cart",cart,new Date());
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('Customer')")
    @GetMapping("/getCart")
    public ResponseEntity<BaseResponseDto> getCart(Authentication authentication) {
        Cart cart = customerService.getCart(authentication);
        BaseResponseDto response = new BaseResponseDto(HttpStatus.CREATED,"Cart as below",cart,new Date());
        return ResponseEntity.ok(response);
    }
}
