package com.aditya.inventory.serviceImpl;

import com.aditya.inventory.customException.InsufficientStocks;
import com.aditya.inventory.customException.ResourceNotFound;
import com.aditya.inventory.dto.CartItemDto;
import com.aditya.inventory.dto.CartResponseDto;
import com.aditya.inventory.dto.ProductDto;
import com.aditya.inventory.entity.Cart;
import com.aditya.inventory.entity.CartItem;
import com.aditya.inventory.entity.Customer;
import com.aditya.inventory.entity.Product;
import com.aditya.inventory.repository.CartRepo;
import com.aditya.inventory.repository.CustomerRepo;
import com.aditya.inventory.repository.ProductRepo;
import com.aditya.inventory.service.CustomerService;
import com.aditya.inventory.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private ProductService productService;







    @Override
    public CartResponseDto addToCart(Integer productId, Authentication authentication) throws FileNotFoundException {
        String email = authentication.getName();
        Customer customer = customerRepo.findByEmail(email);
        Product product = productRepo.findById(productId).get();
       if(product.getQuantity()<=0){
           throw new InsufficientStocks();
       }

        Cart cart = customer.getCart();
        boolean isExits = false;
        if(cart==null){
            cart = new Cart();
            cart.setCustomer(customer);
            cart.setProducts(new ArrayList<>());
            customer.setCart(cart);
        }
            List<CartItem> products = cart.getProducts();
            for(CartItem item:products){
                if(item.getProduct().getProduct_id().equals(productId)){
                    item.setQuantity(item.getQuantity()+1);
                    isExits=true;
                    break;
                }
            }


        if(!isExits){
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            cartItem.setCart(cart);
            products.add(cartItem);
        }

        cart.setPrice(totalPrice(cart.getProducts()));

        customer.setCart(cart);
        customerRepo.save(customer);
        cartRepo.save(cart);


        return getCartResponseDto(cart);
    }

    @Override
    public CartResponseDto addToCart(Integer productId, Authentication authentication, Integer quantity) throws FileNotFoundException {
        String email = authentication.getName();
        Customer customer = customerRepo.findByEmail(email);
        Product product = productRepo.findById(productId).get();
        if(product.getQuantity()<quantity){
            throw new InsufficientStocks();
        }

        Cart cart = customer.getCart();
        boolean isExits = false;
        if(cart==null){
            cart = new Cart();
            cart.setCustomer(customer);
            cart.setProducts(new ArrayList<>());
            customer.setCart(cart);
        }
            List<CartItem> products = cart.getProducts();
            for(CartItem item:products){
                if(item.getProduct().getProduct_id().equals(product.getProduct_id())){
                    item.setQuantity(item.getQuantity()+quantity);
                    isExits=true;
                    break;
                }
            }


        if(!isExits){
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setCart(cart);
            products.add(cartItem);
        }

        cart.setPrice(totalPrice(cart.getProducts()));

        customer.setCart(cart);
        customerRepo.save(customer);
        Cart save = cartRepo.save(cart);

        return getCartResponseDto(save);
    }

    private CartResponseDto getCartResponseDto(Cart save) throws FileNotFoundException {
        CartResponseDto  cartResponseDto = new CartResponseDto();
        List<CartItemDto> cartItemDtos = new ArrayList<>();

        List<ProductDto> productDtos = new ArrayList<>();
        List<CartItem> cartProducts = save.getProducts();
        for(CartItem item:cartProducts){
            ProductDto productById = productService.getProductById(item.getProduct().getProduct_id());
            CartItemDto cartItemDto = new CartItemDto();
            cartItemDto.setProducts(productById);
            cartItemDto.setQuantity((int) item.getQuantity());
            cartItemDtos.add(cartItemDto);

        }
        cartResponseDto.setProducts(cartItemDtos);
        cartResponseDto.setTotal(save.getPrice());

        return cartResponseDto;
    }

    @Override
    public CartResponseDto getCart(Authentication authentication) throws FileNotFoundException {
        String email = authentication.getName();
        Customer customer = customerRepo.findByEmail(email);
        Cart cart = customer.getCart();
        if(cart==null || cart.getProducts()==null || cart.getProducts().isEmpty()){
            throw new ResourceNotFound("No items in Cart");
        }

        return getCartResponseDto(cart);
    }

    @Override
    public CartResponseDto removeProduct(Authentication authentication, Integer productId) throws FileNotFoundException {
        String email = authentication.getName();
        Customer customer = customerRepo.findByEmail(email);
        Cart cart = customer.getCart();

        if (cart == null || cart.getProducts() == null || cart.getProducts().isEmpty()) {
            throw new ResourceNotFound("No items in Cart");
        }

        List<CartItem> products = cart.getProducts();
        Iterator<CartItem> iterator = products.iterator();
        boolean removed = false;

        while (iterator.hasNext()) {
            CartItem item = iterator.next();
            if (item.getProduct().getProduct_id().equals(productId)) {
                item.setCart(null);
                iterator.remove();
                removed = true;
                break;
            }
        }

        if (!removed) {
            throw new ResourceNotFound("Product not found in Cart");
        }

        cart.setPrice(totalPrice(products));


        Cart saved = cartRepo.saveAndFlush(cart);


        if (saved.getProducts().isEmpty()) {
            throw new ResourceNotFound("No items in Cart");
        }

        return getCartResponseDto(saved);
    }



    @Override
    public CartResponseDto decreaseQuantity(Authentication authentication, Integer productId) throws FileNotFoundException {
        String email = authentication.getName();
        Customer customer = customerRepo.findByEmail(email);
        Cart cart = customer.getCart();

        if (cart == null || cart.getProducts() == null || cart.getProducts().isEmpty()) {
            throw new ResourceNotFound("No items in Cart");
        }

        List<CartItem> products = cart.getProducts();
        Iterator<CartItem> iterator = products.iterator();
        boolean updated = false;

        while (iterator.hasNext()) {
            CartItem item = iterator.next();
            if (item.getProduct().getProduct_id().equals(productId)) {
                float currentQty = item.getQuantity();

                if (currentQty > 1) {
                    item.setQuantity(currentQty - 1);
                } else {

                    item.setCart(null);
                    iterator.remove();
                }

                updated = true;
                break;
            }
        }

        if (!updated) {
            throw new ResourceNotFound("Product not found in Cart");
        }

        cart.setPrice(totalPrice(products));
        Cart saved = cartRepo.saveAndFlush(cart);

        if (saved.getProducts().isEmpty()) {
            throw new ResourceNotFound("No items in Cart");
        }

        return getCartResponseDto(saved);
    }


    private float totalPrice(List<CartItem> products){
        float total = 0;
        for(CartItem cartItem : products){
            Product product = cartItem.getProduct();
            float price1 = product.getPrice();
            float quantity = cartItem.getQuantity();
            total=total + (price1*quantity);

        }
        return  total;
    }
}
