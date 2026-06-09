package com.example.demo.services;


import java.util.ArrayList;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dtos.CartItemRequestDTO;
import com.example.demo.dtos.CartResponseDTO;
import com.example.demo.entities.Cart;
import com.example.demo.entities.CartItem;
import com.example.demo.entities.Coupon;
import com.example.demo.entities.Product;
import com.example.demo.entities.User;
import com.example.demo.mappers.CartMapper;
import com.example.demo.repositories.CartRepository;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.repositories.ProductVariantRepository;


import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepo;
    private final ProductRepository productRepo;
    private final ProductVariantRepository variantRepo;
    
    
    private final CouponService couponService;
    
    private final CartMapper cartMapper;

    public CartResponseDTO addItemToCart(User customer, CartItemRequestDTO request) {
        //  Get / Create Cart
        Cart cart = cartRepo.findByCustomer(customer)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setCustomer(customer);
                    return cartRepo.save(newCart);
                });

        if (request.getQuantity() <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }//TODO custom Exception !!
        
        //  is already in the cart ??
        CartItem existingItem = cart.getLignes().stream()
                .filter(item -> item.getProduct().getId().equals(request.getProductId()))
                .filter(item -> {
                	//BOTH NULL ! (or ofc same id)
                	Long itemVariantId = (item.getVariant() != null) ? item.getVariant().getId() : null;
                    return Objects.equals(itemVariantId, request.getVariantId());
                })
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
        	//STOCK !!
        	
        	int totalRequested = existingItem.getQuantite() + request.getQuantity();
            if (totalRequested > existingItem.getProduct().getStock()) {
                throw new RuntimeException("Insufficient stock! Total available: " + existingItem.getProduct().getStock());
            }
            
            // Update quantity if already exists
            existingItem.setQuantite(existingItem.getQuantite() + request.getQuantity());
        } else {
            //  new CartItem
            Product product = productRepo.findById(request.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            
            //STOCK CHECK PLEASEE.. 
            if (request.getQuantity() > product.getStock()) {
                throw new RuntimeException("Insufficient stock! Available stock is : " + product.getStock());
            }
            
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantite(request.getQuantity());
           
            
            if (request.getVariantId() != null) {
            	//bug ! do  not accept null vriant id loool
                newItem.setVariant(variantRepo.findById(request.getVariantId())
                		.orElseThrow(() -> new RuntimeException("Fatal: Variant not found with ID: " + request.getVariantId()))
                		);
            }
            
            cart.getLignes().add(newItem);
        }

        Cart savedCart = cartRepo.save(cart);
        return cartMapper.toResponseDTO(savedCart); // dto
    }
    
    //getCart 
    public CartResponseDTO getCart(User customer) {
    	//if has cart -> find it 
    	//else new empty DTO
    	
    	return cartRepo.findByCustomer(customer)
                .map(cartMapper::toResponseDTO)
                .orElseGet(() -> {
                	CartResponseDTO emptyCart = new CartResponseDTO();
                    emptyCart.setItems(new ArrayList<>());
                    emptyCart.setTotalCartPrice(0.0);
                    emptyCart.setDiscountAmount(0.0);
                    emptyCart.setFinalPrice(0.0);
                    return emptyCart;
                });
    }
    
    //update quantity
    public CartResponseDTO updateItemQuantity(User customer, Long itemId, Integer newQuantity) {
    	//which cart..
    	Cart cart = cartRepo.findByCustomer(customer)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    	
    	//ooh the filter
    	CartItem item = cart.getLignes().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found in the cart"));
    	
    	//STOCK 
    	if (newQuantity > item.getProduct().getStock()) {
            throw new RuntimeException("Only " + item.getProduct().getStock() + " units left in stock!");
        }
    	
    	if (newQuantity <= 0) {
            // 0 = remove i guess ?
            cart.getLignes().remove(item);
        } else {
            item.setQuantite(newQuantity);
        }
    	
    	Cart savedCart = cartRepo.save(cart);
        return cartMapper.toResponseDTO(savedCart);
    }
    
    //delete item from cart -> return the new cart !!
    public CartResponseDTO removeItemFromCart(User customer, Long itemId) {
    	Cart cart = cartRepo.findByCustomer(customer)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    	
    	//again find item..
    	CartItem itemToRemove = cart.getLignes().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found in your cart"));
    	
    	cart.getLignes().remove(itemToRemove);
    	
    	Cart savedCart = cartRepo.save(cart);
        return cartMapper.toResponseDTO(savedCart);
        
        //ORPHAN REMOVAL in cart.java!! 
        //item is DELTEED automaitcally in DB !
        //lese it satys with cart_id NULL
    	
    }
    
    //coupon stuff.. 
    public CartResponseDTO applyCoupon(User customer, String code) {
    	CartResponseDTO dto = getCart(customer);
    	//valid ? 
    	Coupon coupon = couponService.isCouponValid(code);
    	
    	
    	double discount = 0.0;
        if ("PERCENT".equalsIgnoreCase(coupon.getType())) {
            discount = dto.getTotalCartPrice() * (coupon.getValeur() / 100.0);
        } else if ("FIXED".equalsIgnoreCase(coupon.getType())) {
            discount = coupon.getValeur();
        }
        //.equalsIgnoreCase is crazyy 
        
        discount = Math.min(discount, dto.getTotalCartPrice());
        
        //new dto  coupons.. 
        dto.setAppliedCouponCode(coupon.getCode());
        dto.setDiscountAmount(discount);
        dto.setFinalPrice(dto.getTotalCartPrice() - discount);
        
        return dto;
    	
    }
    
    //and to remove the coupon... 
    public CartResponseDTO removeCoupon(User customer) {
    	CartResponseDTO dto = getCart(customer);
        dto.setFinalPrice(dto.getTotalCartPrice()); // Reset !!!!!
        return dto;
    }
    
    
 
    
}
