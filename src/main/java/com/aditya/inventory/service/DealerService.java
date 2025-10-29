package com.aditya.inventory.service;

import com.aditya.inventory.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

public interface DealerService {

    Page<Product> getStock(Authentication authentication,int pageNo,int pageSize);

}
