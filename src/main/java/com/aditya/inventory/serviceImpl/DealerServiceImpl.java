package com.aditya.inventory.serviceImpl;

import com.aditya.inventory.customException.ResourceNotFound;
import com.aditya.inventory.entity.*;
import com.aditya.inventory.repository.DealerRepo;
import com.aditya.inventory.repository.ProductRepo;
import com.aditya.inventory.service.DealerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class DealerServiceImpl implements DealerService {

    @Autowired
    private DealerRepo dealerRepo;

    @Autowired
    private ProductRepo productRepo;

    @Override
    public Page<Product> getStock( Authentication authentication,int page, int pageSize) {
        String email = authentication.getName();
        Dealer dealer = dealerRepo.findByEmail(email);
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Product> stock = productRepo.findByDealer(dealer, pageable);
        if (stock.isEmpty()) {
            throw new ResourceNotFound("Stoks are empty");
        }

        return stock;
    }

}
