package com.aditya.inventory.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.aditya.inventory.customException.AlreadyExits;
import com.aditya.inventory.customException.InsufficientStocks;
import com.aditya.inventory.customException.ResourceNotFound;
import com.aditya.inventory.entity.Dealer;
import com.aditya.inventory.repository.DealerRepo;
import com.aditya.inventory.repository.UserRepo;
import com.aditya.inventory.service.DealerService;
import com.aditya.inventory.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.aditya.inventory.dto.ProductDto;
import com.aditya.inventory.entity.Category;
import com.aditya.inventory.entity.Product;
import com.aditya.inventory.entity.TransactionalLog;
import com.aditya.inventory.mapper.ProductMapper;
import com.aditya.inventory.repository.CategoryRepo;
import com.aditya.inventory.repository.ProductRepo;
import com.aditya.inventory.service.ProductService;
import com.aditya.inventory.service.TransactionService;


@Service
public class ProductServiceImpl implements ProductService{

	@Autowired
	ProductRepo productRepo;

	@Autowired
	CategoryRepo categoryRepo;

	@Autowired
	ProductMapper productMapper;

    @Autowired
    DealerService dealerService;

	@Autowired
	TransactionService transactionService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private DealerRepo dealerRepo;

    //--------------- Create and Updates----------------//

	
	//create
	public ProductDto addProduct(ProductDto productDto,Authentication authentication) {
        String dealerName = authentication.getName();
        Dealer dealer = dealerRepo.findByEmail(dealerName);
        existsByName(productDto.getName());
        newCategory(productDto);

        Product product = productMapper.toProduct(productDto);
        product.setDealer(dealer);
		product.setCreatedAt(new Date());

		Product save = productRepo.save(product);

		return productMapper.toDto(save);

	}

    //Update
	public ProductDto updateProduct(Integer id, ProductDto productDto,Authentication authentication) {

        String name = authentication.getName();
        Dealer dealer = dealerRepo.findByEmail(name);
        if(productDto.getCategories()!=null ) {
            if (!productDto.getCategories().isEmpty()) {
                newCategory(productDto);
            }
        }

        Product	product = productRepo.findById(id).get();
        if(productRepo.findById(id).isEmpty()) throw new ResourceNotFound("Product not found");

        if( !product.getDealer().getDealer_id().equals(dealer.getDealer_id())){
            throw new AuthenticationCredentialsNotFoundException("Dealer is different for this porduct");
        }

        Product	product2 = productMapper.toProduct(productDto, product);
        product2.setDealer(dealer);
        product2.setUpdatedAt(new Date());
			productRepo.save(product2);

		return productMapper.toDto(product2);

	}

    //Check categories and new
    private void newCategory(ProductDto productDto) {
        List<String> categories = productDto.getCategories();
        for (String category : categories) {
            if (!categoryRepo.existsByName(category)) {
                Category category2 = new Category();
                category2.setName(category);
                categoryRepo.save(category2);
            }
        }
    }


    //-------------------------- Transaction----------------------//
	
		//Stock Update
		public TransactionalLog updateStock(Authentication authentication,Integer productId, int stockToUpdate) {

            String email = authentication.getName();
            Dealer dealer = dealerRepo.findByEmail(email);
            if(productRepo.findById(productId).isEmpty()) throw new ResourceNotFound("Product not found");




            Product	product = productRepo.findById(productId).get();

            if( !product.getDealer().getDealer_id().equals(dealer.getDealer_id())){
                throw new AuthenticationCredentialsNotFoundException("Dealer is different for this porduct");
            }

            if((int)product.getQuantity()+stockToUpdate<0){
                throw new InsufficientStocks();
            }

            product.setQuantity(product.getQuantity() + stockToUpdate);
            product.setUpdatedAt(new Date());

			TransactionalLog log = new TransactionalLog();
			log.setDealer_id(dealer.getDealer_id());
			log.setProductId(productId);
			String type = stockToUpdate > 0 ? "Increase" : "Decrease";
			log.setChangeType(type);

            log.setQuantityChange(stockToUpdate);
			productRepo.save(product);
			TransactionalLog log2 = transactionService.createLog(log);
			return log2;

		}

	//-----------------------Finds----------------------------//

	// By name
	public void existsByName(String name) {
        if(productRepo.existsByName(name)){
           throw new AlreadyExits(name+" this Product ");
        }
	}

	// ByID
	public ProductDto getProductById(Integer id) {
		Product product = productRepo.findById(id).get();
        if(productRepo.findById(id).isEmpty()) throw new ResourceNotFound("Product not found");
		ProductDto productDto = productMapper.toDto(product);
		return productDto;

	}

	// ALL
	public Page<Product> getProducts(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page,pageSize);
		Page<Product> products = productRepo.findAll(pageable);
	    if (products == null || products.isEmpty()) throw new ResourceNotFound("Product not added yet");
		return products;

    }
	
	//filtering
	
	public Page<Product> getProductsbyBrand(String brandName,int page, int pageSize) {
        Pageable pageable = PageRequest.of(page,pageSize);
        Page<Product> products = productRepo.findByBrand(brandName,pageable);
        if (products == null || products.isEmpty()) throw new ResourceNotFound("Product not found of "+ brandName +" this brand");
		return products;

	}

	
	public Page<Product> getProductsbyCategory(String categoryName,int page, int pageSize) {
        Pageable pageable = PageRequest.of(page,pageSize);
        Page<Product> products = productRepo.findByCategories_Name(categoryName,pageable);
        if (products == null || products.isEmpty()) throw new ResourceNotFound("Product not fount of category  " + categoryName);
		return products;

	}
	
	
	public Page<Product> getProductsWithLowStock(int page, int pageSize,Authentication authentication) {
        String name = authentication.getName();
        Dealer dealer = dealerRepo.findByEmail(name);
        Pageable pageable = PageRequest.of(page,pageSize);
        Page<Product> products = productRepo.getLowStockProducts(pageable,dealer);
        if (products == null || products.isEmpty()) throw new ResourceNotFound("No low Stock products found");
		return products;
	}

    @Override
    public Page<Product> getProductsByName(String name, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page,pageSize);
        Page<Product> products = productRepo.findByName(name,pageable);
        if (products == null || products.isEmpty()) throw new ResourceNotFound("Product not found of "+ name +" this Name");
        return products;
    }

    //Sort
	
	public Page<Product> getProductsInRange(double from ,double to,int page, int pageSize) {
        Pageable pageable = PageRequest.of(page,pageSize);
        Page<Product> products = productRepo.findByPriceBetween(from,to,pageable);
        if (products == null || products.isEmpty()) throw new ResourceNotFound("Product not fount in range "+from+" - "+to);
		return products;

	}
	

	//--------------------------- Delete-----------------------------//

	public boolean deleteProduct(Integer id,Authentication authentication) {
        String name = authentication.getName();
        Dealer dealer = dealerRepo.findByEmail(name);
        Product product = productRepo.findById(id).get();
        if( !product.getDealer().getDealer_id().equals(dealer.getDealer_id())){
            throw new AuthenticationCredentialsNotFoundException("Dealer is different for this porduct");
        }
        if(productRepo.findById(id).isEmpty()) throw new ResourceNotFound("Product not found");
        productRepo.delete(product);


		return true;

	}

	
	//---------------------For Pagination--------------------//
	
//	public HashMap<Integer, List<Product>> pagination(List<Product> products){
//		HashMap<Integer, List<Product>> allProducts =  new HashMap<>();
//		int pageSize = 5;
//		int pageNo = 1;
//		int lastIndex = pageSize;
//
//		for (int i = 0; i < products.size(); i += pageSize) {
//		    if (lastIndex > products.size()) {
//		        lastIndex = products.size();
//		    }
//
//		    allProducts.put(pageNo, products.subList(i, lastIndex));
//		    pageNo++;
//		    lastIndex += pageSize;
//		}
//
//		return allProducts;
//	}

	

}
