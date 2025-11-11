package com.aditya.inventory.service;

import com.aditya.inventory.dto.ProductDto;
import com.aditya.inventory.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

import java.io.FileNotFoundException;

public interface DealerService {

    Page<ProductDto> getStock(Authentication authentication, int pageNo, int pageSize) throws FileNotFoundException;

}
