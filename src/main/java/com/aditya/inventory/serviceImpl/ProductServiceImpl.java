package com.aditya.inventory.serviceImpl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.aditya.inventory.customException.AlreadyExits;
import com.aditya.inventory.customException.InsufficientStocks;
import com.aditya.inventory.customException.ResourceNotFound;
import com.aditya.inventory.entity.*;
import com.aditya.inventory.repository.DealerRepo;
import com.aditya.inventory.repository.UserRepo;
import com.aditya.inventory.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.aditya.inventory.dto.ProductDto;
import com.aditya.inventory.mapper.ProductMapper;
import com.aditya.inventory.repository.CategoryRepo;
import com.aditya.inventory.repository.ProductRepo;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepo productRepo;

    @Autowired
    CategoryRepo categoryRepo;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    ImageService imageService;

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

    @Value("${project.image}")
    private String path;

    //--------------- Create and Updates----------------//

    // create
    public ProductDto addProduct(ProductDto productDto, Authentication authentication) throws IOException {
        String dealerName = authentication.getName();
        Dealer dealer = dealerRepo.findByEmail(dealerName);
        if (dealer == null) {
            throw new ResourceNotFound("Dealer not found");
        }

        existsByName(productDto.getName());

        if (productDto.getCategories() != null && !productDto.getCategories().isEmpty()) {
            newCategory(productDto);
        }

        Product product = productMapper.toProduct(productDto);

        product.setDealer(dealer);
        product.setCreatedAt(new Date());

        Product save = productRepo.save(product);

        return productMapper.toDto(save);
    }

    // Update
    public ProductDto updateProduct(Integer id, ProductDto productDto, Authentication authentication) {

        String name = authentication.getName();
        Dealer dealer = dealerRepo.findByEmail(name);
        if (dealer == null) {
            throw new ResourceNotFound("Dealer not found");
        }

        if (productDto.getCategories() != null && !productDto.getCategories().isEmpty()) {
            newCategory(productDto);
        }

        Optional<Product> productOpt = productRepo.findById(id);
        if (productOpt.isEmpty()) throw new ResourceNotFound("Product not found");

        Product product = productOpt.get();

        if (!Objects.equals(product.getDealer().getDealer_id(), dealer.getDealer_id())) {
            throw new AuthenticationCredentialsNotFoundException("Dealer is different for this product");
        }

        Product product2 = productMapper.toProduct(productDto, product);
        product2.setDealer(dealer);
        product2.setUpdatedAt(new Date());
        productRepo.save(product2);

        return productMapper.toDto(product2);

    }

    // Check categories and new
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

    // Stock Update
    public TransactionalLog updateStock(Authentication authentication, Integer productId, int stockToUpdate) {

        String email = authentication.getName();
        Dealer dealer = dealerRepo.findByEmail(email);
        if (dealer == null) {
            throw new ResourceNotFound("Dealer not found");
        }

        Optional<Product> productOpt = productRepo.findById(productId);
        if (productOpt.isEmpty()) throw new ResourceNotFound("Product not found");

        Product product = productOpt.get();

        if (!Objects.equals(product.getDealer().getDealer_id(), dealer.getDealer_id())) {
            throw new AuthenticationCredentialsNotFoundException("Dealer is different for this product");
        }

        if ((int) product.getQuantity() + stockToUpdate < 0) {
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
        if (productRepo.existsByName(name)) {
            throw new AlreadyExits(name + " this Product ");
        }
    }

    // ByID
    public ProductDto getProductById(Integer id) throws FileNotFoundException {
        Optional<Product> productOpt = productRepo.findById(id);
        if (productOpt.isEmpty()) throw new ResourceNotFound("Product not found");
        Product product = productOpt.get();
        ProductDto productDto = productMapper.toDto(product);
        productDto.setImages(getImages(product));
        return productDto;

    }

    // ALL
    public Page<ProductDto> getProducts(int page, int pageSize) throws FileNotFoundException {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Product> products = productRepo.findAll(pageable);
        if (products == null || products.isEmpty()) throw new ResourceNotFound("Product not added yet");
        return getProductDtos(pageable, products);

    }

    // filtering

    public Page<ProductDto> getProductsbyBrand(String brandName, int page, int pageSize) throws FileNotFoundException {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Product> products = productRepo.findByBrand(brandName, pageable);
        if (products == null || products.isEmpty())
            throw new ResourceNotFound("Product not found of " + brandName + " this brand");
        return getProductDtos(pageable, products);

    }


    public Page<ProductDto> getProductsbyCategory(String categoryName, int page, int pageSize) throws FileNotFoundException {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Product> products = productRepo.findByCategories_Name(categoryName, pageable);
        if (products == null || products.isEmpty())
            throw new ResourceNotFound("Product not found of category  " + categoryName);
        return getProductDtos(pageable, products);

    }


    public Page<ProductDto> getProductsWithLowStock(int page, int pageSize, Authentication authentication) throws FileNotFoundException {
        String name = authentication.getName();
        Dealer dealer = dealerRepo.findByEmail(name);
        if (dealer == null) throw new ResourceNotFound("Dealer not found");
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Product> products = productRepo.getLowStockProducts(pageable, dealer);
        if (products == null || products.isEmpty()) throw new ResourceNotFound("No low Stock products found");
        return getProductDtos(pageable, products);
    }

    @Override
    public Page<ProductDto> getProductsByName(String name, int page, int pageSize) throws FileNotFoundException {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Product> products = productRepo.findByName(name, pageable);
        if (products == null || products.isEmpty())
            throw new ResourceNotFound("Product not found of " + name + " this Name");
        return getProductDtos(pageable, products);
    }

    // Sort

    public Page<ProductDto> getProductsInRange(double from, double to, int page, int pageSize) throws FileNotFoundException {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Product> products = productRepo.findByPriceBetween(from, to, pageable);
        if (products == null || products.isEmpty())
            throw new ResourceNotFound("Product not found in range " + from + " - " + to);
        return getProductDtos(pageable, products);

    }

    private Page<ProductDto> getProductDtos(Pageable pageable, Page<Product> products) throws FileNotFoundException {
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product product : products) {
            ProductDto dto = productMapper.toDto(product);
            List<String> images = getImages(product);
            dto.setImages(images);

            productDtos.add(dto);
        }
        return new PageImpl<ProductDto>(productDtos, pageable, products.getTotalElements());
    }


    //--------------------------- Delete-----------------------------//

    public boolean deleteProduct(Integer id, Authentication authentication) {
        String name = authentication.getName();
        Dealer dealer = dealerRepo.findByEmail(name);
        if (dealer == null) {
            throw new ResourceNotFound("Dealer not found");
        }

        Optional<Product> productOpt = productRepo.findById(id);
        if (productOpt.isEmpty()) throw new ResourceNotFound("Product not found");

        Product product = productOpt.get();

        if (!Objects.equals(product.getDealer().getDealer_id(), dealer.getDealer_id())) {
            throw new AuthenticationCredentialsNotFoundException("Dealer is different for this product");
        }

        productRepo.delete(product);

        return true;

    }

    // For images

    private List<String> getImages(Product product) throws FileNotFoundException {
        List<String> images = new ArrayList<>();
        List<FileData> images1 = product.getImages();
        if (images1 == null || images1.isEmpty()) {
            String image = "No images added yet";
            images.add(image);
        } else {
            for (FileData fileData : images1) {
                String image = imageService.getImage(fileData);
                images.add(image);
            }
        }
        return images;
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