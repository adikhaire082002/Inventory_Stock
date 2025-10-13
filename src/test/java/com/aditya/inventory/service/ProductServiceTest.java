package com.aditya.inventory.service;

import com.aditya.inventory.dto.ProductDto;
import com.aditya.inventory.entity.Product;
import com.aditya.inventory.entity.TransactionalLog;
import com.aditya.inventory.mapper.ProductMapper;
import com.aditya.inventory.repository.CategoryRepo;
import com.aditya.inventory.repository.ProductRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    ProductService productService;

    @Mock
    ProductRepo productRepo;

    @Mock
    CategoryRepo categoryRepo;

    @Mock
    ProductMapper mapper;

    @Mock
    TransactionService transactionService;

    // ------------------- addProduct -------------------
    @Test
    void addProductShouldAddProductSuccessfully() {
        ProductDto dto = new ProductDto();
        dto.setBrand("Titan");
        dto.setCategories(List.of("Watch", "Luxury"));
        dto.setName("Royal");
        dto.setDescription("Premium Watch");
        dto.setPrice(25000);

        Product entity = new Product();

        when(categoryRepo.existsByName(anyString())).thenReturn(true);
        when(mapper.toProduct(dto)).thenReturn(entity);
        when(productRepo.save(entity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(dto);

        ProductDto result = productService.addProduct(dto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Titan", result.getBrand());

        System.out.println("Product added successfully!");
    }

    // ------------------- updateProduct -------------------
    @Test
    void updateProductShouldUpdateSuccessfully() {
        ProductDto dto = new ProductDto();
        dto.setCategories(List.of("Watch"));
        dto.setName("Updated Watch");

        Product existing = new Product();
        Product updated = new Product();

        when(categoryRepo.existsByName(anyString())).thenReturn(true);
        when(productRepo.findById(1)).thenReturn(Optional.of(existing));
        when(mapper.toProduct(dto, existing)).thenReturn(updated);
        when(productRepo.save(updated)).thenReturn(updated);
        when(mapper.toDto(updated)).thenReturn(dto);

        ProductDto result = productService.updateProduct(1, dto);

        Assertions.assertEquals("Updated Watch", result.getName());
        System.out.println("Product updated successfully!");
    }

    @Test
    void updateProductShouldThrowExceptionIfNotFound() {
        ProductDto dto = new ProductDto();
        dto.setCategories(new ArrayList<>());
        when(productRepo.findById(99)).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class, () -> {
            productService.updateProduct(99, dto);
        });

        System.out.println("Product not found for update — exception thrown as expected!");
    }

    // ------------------- updateStock -------------------
    @Test
    void updateStockShouldUpdateQuantityAndLog() {
        Product product = new Product();
        product.setQuantity(10);
        product.setDealers(new ArrayList<>());

        TransactionalLog log = new TransactionalLog();
        log.setProductId(1);
        log.setUserID(100);

        when(productRepo.findById(1)).thenReturn(Optional.of(product));
        when(productRepo.save(product)).thenReturn(product);
        when(transactionService.createLog(any())).thenReturn(log);

        productService.updateStock(100, 1, 5);

        Assertions.assertEquals(15, product.getQuantity());
        Assertions.assertTrue(product.getDealers().contains(100));

        System.out.println("Product stock updated and transaction log created!");
    }

    @Test
    void updateStockShouldThrowExceptionIfProductNotFound() {
        when(productRepo.findById(42)).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class, () -> {
            productService.updateStock(100, 42, 5);
        });

        System.out.println("Product not found for stock update — exception thrown as expected!");
    }

    // ------------------- getProductById -------------------
    @Test
    void getProductByIdShouldReturnDto() {
        Product entity = new Product();
        ProductDto dto = new ProductDto();

        when(productRepo.findById(1)).thenReturn(Optional.of(entity));
        when(mapper.toDto(entity)).thenReturn(dto);

        ProductDto result = productService.getProductById(1);

        Assertions.assertNotNull(result);
        System.out.println("Product fetched successfully by ID!");
    }

    @Test
    void getProductByIdShouldThrowIfNotFound() {
        when(productRepo.findById(10)).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class, () -> {
            productService.getProductById(10);
        });

        System.out.println("Product not found by ID — exception thrown as expected!");
    }

    // ------------------- deleteProduct -------------------
    @Test
    void deleteProductShouldDeleteAndReturnDto() {
        Product entity = new Product();
        entity.setCategories(new ArrayList<>());
        ProductDto dto = new ProductDto();

        when(productRepo.findById(1)).thenReturn(Optional.of(entity));
        when(mapper.toDto(entity)).thenReturn(dto);

        ProductDto result = productService.deleteProduct(1);

        Assertions.assertNotNull(result);
        System.out.println("Product deleted successfully!");
    }

    @Test
    void deleteProductShouldThrowIfNotFound() {
        when(productRepo.findById(5)).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class, () -> {
            productService.deleteProduct(5);
        });

        System.out.println("Product not found for delete — exception thrown as expected!");
    }

    // ------------------- pagination -------------------
    @Test
    void paginationShouldSplitPagesCorrectly() {
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < 11; i++) products.add(new Product());
        var result = productService.pagination(products);
        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals(5, result.get(1).size());
        System.out.println("Pagination logic verified successfully!");
    }
}
