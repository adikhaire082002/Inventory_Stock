package com.aditya.inventory.service;

import com.aditya.inventory.dto.CartResponseDto;
import org.springframework.security.core.Authentication;

import java.io.FileNotFoundException;

public interface CustomerService {

    CartResponseDto addToCart(Integer productId, Authentication authentication) throws FileNotFoundException;

    CartResponseDto addToCart(Integer productId, Authentication authentication, Integer quantity) throws FileNotFoundException;

    CartResponseDto getCart(Authentication authentication) throws FileNotFoundException;

    CartResponseDto removeProduct(Authentication authentication, Integer productId) throws FileNotFoundException;
    CartResponseDto decreaseQuantity(Authentication authentication, Integer productId) throws FileNotFoundException;

}
