package com.aditya.inventory.service;

import com.aditya.inventory.entity.Cart;
import org.springframework.security.core.Authentication;

public interface CustomerService {

    Cart addToCart(Integer productId, Authentication authentication);

    Cart addToCart(Integer productId, Authentication authentication,Integer quantity);

    Cart getCart(Authentication authentication);

    Cart removeProduct(Authentication authentication,Integer productId);
    Cart decreaseQuantity(Authentication authentication,Integer productId);

}
