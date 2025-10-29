package com.aditya.inventory.repository;

import java.util.List;

import com.aditya.inventory.entity.Dealer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.aditya.inventory.entity.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer> {

	boolean existsByName(String name);

    Page<Product> findAll(Pageable  pageable);

    @Query("Select p from Product p where p.quantity < 10 and p.dealer = :dealer ")
    Page<Product> getLowStockProducts(Pageable  pageable,@Param("dealer") Dealer dealer);

    Page<Product> findByBrand(String brandName, Pageable  pageable);

    Page<Product> findByCategories_Name(String categoryName,Pageable  pageable);

    Page<Product> findByPriceBetween(double from, double to, Pageable pageable);

    Page<Product> findByName(String name, Pageable  pageable);


    Page<Product> findByDealer(Dealer dealer,Pageable  pageable);
}
