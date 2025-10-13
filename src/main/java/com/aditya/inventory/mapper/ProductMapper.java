package com.aditya.inventory.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aditya.inventory.dto.ProductDto;
import com.aditya.inventory.entity.Category;
import com.aditya.inventory.entity.Product;
import com.aditya.inventory.repository.CategoryRepo;

@Component
public class ProductMapper {

	@Autowired
	private CategoryRepo categoryRepo;
	
	
	//------------------Product to ProductDTO-----------------//

	public ProductDto toDto(Product product) {
		List<String> categories = new ArrayList<String>();

		for (Category category : product.getCategories()) {
			categories.add(category.getName());

		}
		ProductDto productDto = new ProductDto();

		productDto.setBrand(product.getBrand());
		productDto.setCategories(categories);
		productDto.setCreatedAt(product.getCreatedAt());
		productDto.setDescription(product.getDescription());
		productDto.setName(product.getName());
		productDto.setPrice(product.getPrice());
		productDto.setQuantity(product.getQuantity());
		productDto.setUpdatedAt(product.getUpdatedAt());

		return productDto;
	}
	
	//-------------------ProductDTO to Product-----------------------//

	public Product toProduct(ProductDto dto) {

		List<Category> categories = new ArrayList<Category>();

		for (String category : dto.getCategories()) {
			categories.add(categoryRepo.findByName(category));

		}

		Product product = new Product();
		product.setBrand(dto.getBrand());
		product.setCategories(categories);
		product.setCreatedAt(dto.getCreatedAt());
		product.setDescription(dto.getDescription());
		product.setName(dto.getName());
		product.setPrice(dto.getPrice());
		product.setQuantity(dto.getQuantity());
		product.setUpdatedAt(dto.getUpdatedAt());

		return product;
	}

	
	//-----------------Exixted ProductDTO to Prouct---------------//
	public Product toProduct(ProductDto dto, Product product) {

		List<Category> categories = new ArrayList<Category>();

		for (String category : dto.getCategories()) {
			categories.add(categoryRepo.findByName(category));

		}

		product.setBrand(dto.getBrand());
		product.setCategories(categories);
		product.setDescription(dto.getDescription());
		product.setName(dto.getName());
		product.setPrice(dto.getPrice());
		product.setQuantity(dto.getQuantity());
		product.setUpdatedAt(dto.getUpdatedAt());

		return product;
	}

}
