package com.aditya.inventory.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aditya.inventory.dto.ProductDto;
import com.aditya.inventory.entity.Category;
import com.aditya.inventory.entity.Product;
import com.aditya.inventory.entity.TransactionalLog;
import com.aditya.inventory.mapper.ProductMapper;
import com.aditya.inventory.repository.CategoryRepo;
import com.aditya.inventory.repository.ProductRepo;


@Service
public class ProductService {

	@Autowired
	ProductRepo productRepo;

	@Autowired
	CategoryRepo categoryRepo;

	@Autowired
	ProductMapper productMapper;

	@Autowired
	TransactionService transactionService;

	//--------------- Create and Updates----------------//

	
	//create
	public ProductDto addProduct(ProductDto productDto) {
		List<String> categories = productDto.getCategories();
		for (String category : categories) {
			if (!categoryRepo.existsByName(category)) {
				Category category2 = new Category();
				category2.setName(category);
				categoryRepo.save(category2);
			}
		}

		Product product = productMapper.toProduct(productDto);
		product.setCreatedAt(new Date());

		Product save = productRepo.save(product);

		return productMapper.toDto(save);

	}

	
	//Update
	public ProductDto updateProduct(Integer id, ProductDto productDto) {

		List<String> categories = productDto.getCategories();
		for (String category : categories) {
			if (!categoryRepo.existsByName(category)) {
				Category category2 = new Category();
				category2.setName(category);
				categoryRepo.save(category2);
			}
		}
	
		Product	product = productRepo.findById(id).get();
		Product	product2 = productMapper.toProduct(productDto, product);
			product2.setUpdatedAt(new Date());
			productRepo.save(product2);

		return productMapper.toDto(product2);

	}
	
	
	//-------------------------- Transaction----------------------//
	
		//Stock Update
		public TransactionalLog updateStock(Integer userID, Integer productId, int stockToUpdate) {
			TransactionalLog log = new TransactionalLog();
			log.setUserID(userID);
			log.setProductId(productId);
			String type = stockToUpdate > 0 ? "Increase" : "Decrease";
			log.setChangeType(type);
			
			Product	product = productRepo.findById(productId).get();
			product.setQuantity(product.getQuantity() + stockToUpdate);
			log.setQuantityChange(stockToUpdate);
			
			List<Integer> dealers = product.getDealers();
			if (dealers == null) {
				dealers = new ArrayList<>();
			}
			dealers.add(userID);
			product.setDealers(dealers);
			productRepo.save(product);
			TransactionalLog log2 = transactionService.createLog(log);
			return log2;

		}

	//-----------------------Finds----------------------------//

	// By name
	public boolean existsByName(String name) {
		return productRepo.existsByName(name);
	}

	// ByID
	public ProductDto getProductById(Integer id) {
		Product product = null;

			product = productRepo.findById(id).get();

	
		ProductDto productDto = productMapper.toDto(product);
		return productDto;

	}

	// ALL
	public HashMap<Integer, List<Product>> getProducts() {
		List<Product> products = productRepo.getProducts();
	
		return pagination(products);

	}
	
	//filtering
	
	public HashMap<Integer, List<Product>> getProductsbyBrand(String brandName) {
		List<Product> products = productRepo.findByBrand(brandName);
		return pagination(products);

	}
	
	public HashMap<Integer, List<Product>> getProductsbyCategory(String categoryName) {
		List<Product> products = productRepo.findByCategories_Name(categoryName);
		return pagination(products);

	}
	
	
	public List<Product> getProductsWithLowStock() {
		List<Product> products = productRepo.getLowStockProducts();
		return products;
	}
	
	//Sort
	
	public HashMap<Integer, List<Product>> getProductsInRange(double from ,double to) {
		List<Product> products = productRepo.findByPriceBetween(from,to);
		return pagination(products);

	}
	

	//--------------------------- Delete-----------------------------//

	public ProductDto deleteProduct(Integer id) {
		Product product = null;

			product = productRepo.findById(id).get();
			product.getCategories().size();
			productRepo.delete(product);


		return productMapper.toDto(product);

	}

	
	//---------------------For Pagination--------------------//
	
	public HashMap<Integer, List<Product>> pagination(List<Product> products){
		HashMap<Integer, List<Product>> allProducts =  new HashMap<>();
		int pageSize = 5;
		int pageNo = 1;
		int lastIndex = pageSize;

		for (int i = 0; i < products.size(); i += pageSize) {
		    if (lastIndex > products.size()) {
		        lastIndex = products.size();
		    }

		    allProducts.put(pageNo, products.subList(i, lastIndex));
		    pageNo++;
		    lastIndex += pageSize;
		}
		
		return allProducts;
	}

	

}
