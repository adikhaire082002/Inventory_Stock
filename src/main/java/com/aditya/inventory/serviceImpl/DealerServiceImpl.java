package com.aditya.inventory.serviceImpl;

import com.aditya.inventory.customException.ResourceNotFound;
import com.aditya.inventory.dto.ProductDto;
import com.aditya.inventory.entity.*;
import com.aditya.inventory.mapper.ProductMapper;
import com.aditya.inventory.repository.DealerRepo;
import com.aditya.inventory.repository.ProductRepo;
import com.aditya.inventory.service.DealerService;
import com.aditya.inventory.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DealerServiceImpl implements DealerService {

    @Autowired
    private DealerRepo dealerRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ProductMapper  productMapper;

    @Autowired
    private ImageService imageService;

    @Override
    public Page<ProductDto> getStock(Authentication authentication, int page, int pageSize) throws FileNotFoundException {
        String email = authentication.getName();
        Dealer dealer = dealerRepo.findByEmail(email);
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Product> stock = productRepo.findByDealer(dealer, pageable);
        if (stock.isEmpty()) {
            throw new ResourceNotFound("Stoks are empty");
        }
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product product : stock) {
            ProductDto dto = productMapper.toDto(product);
            List<String> images = getImages(product);
            dto.setImages(images);

            productDtos.add(dto);
        }
        return new PageImpl<ProductDto>(productDtos,pageable,stock.getTotalElements());
    }

    //For images

    private List<String> getImages(Product product) throws FileNotFoundException {
        List<String> images = new ArrayList<>();
        List<FileData> images1 = product.getImages();
        if(images1 == null || images1.isEmpty()) {
            String image  =  "No images added yet";
            images.add(image);
        }else {
            for (FileData fileData : images1) {
                String image = imageService.getImage(fileData);
                images.add(image);
            }
        }
        return images;
    }


}
