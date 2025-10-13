package com.aditya.inventory.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aditya.inventory.entity.Category;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Integer> {

	Category findByName(String category);

	boolean existsByName(String category);

}
