package com.example.demo.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.CartItemRequestDTO;
import com.example.demo.dtos.CartResponseDTO;
import com.example.demo.entities.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.CartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
	private final CartService cartService;
	private final UserRepository userRepo;
	
	//ajouter article
	@PostMapping("/items")
	public ResponseEntity<CartResponseDTO> addItem(@RequestBody CartItemRequestDTO request) {
		//WHO IS THE USER ?? 
		//-> security context holder
		String  email = SecurityContextHolder.getContext().getAuthentication().getName();
		
		User currentUser = userRepo.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("User cannot be found"));
				
		
		
		CartResponseDTO response = cartService.addItemToCart(currentUser, request);
		return ResponseEntity.ok(response);
		
	}
	
	//see the cart: 
	@GetMapping
	public ResponseEntity<CartResponseDTO> getCart(){
		//who ? 
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
	    User currentUser = userRepo.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("User not found"));
	    
	    return ResponseEntity.ok(cartService.getCart(currentUser));
		
	}
	
	//update a quantity  PUT /api/cart/items/{itemId} — modifier quantité
	@PutMapping("/items/{itemId}")
	public ResponseEntity<CartResponseDTO> updateQuantity(
			@PathVariable Long itemId,
	        @RequestBody Integer newQuantity){
		//wait dto or .. just send item id + quantity 
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
	    User currentUser = userRepo.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("User not found"));
	    
	    CartResponseDTO response = cartService.updateItemQuantity(currentUser, itemId, newQuantity);
	    return ResponseEntity.ok(response);
		
		
	}
	
	//delete article DELETE /api/cart/items/{itemId} — retirer un article
	@DeleteMapping("/items/{itemId}")
	public ResponseEntity<CartResponseDTO> removeItem(@PathVariable Long itemId) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
	    User currentUser = userRepo.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("User not found"));
	    
	    CartResponseDTO response = cartService.removeItemFromCart(currentUser, itemId);
	    return ResponseEntity.ok(response);
	}
	
	//coupns stuffs..
	
	//->/api/cart/coupon
	@PostMapping("/coupon")
	public ResponseEntity<CartResponseDTO> applyCoupon(@RequestBody Map<String, String> request) {
	    String email = SecurityContextHolder.getContext().getAuthentication().getName();
	    User currentUser = userRepo.findByEmail(email).orElseThrow();

	    String code = request.get("code");
	    
	    return ResponseEntity.ok(cartService.applyCoupon(currentUser, code));
	}
	

	
	// DELETE-> /api/cart/coupon
	@DeleteMapping("/coupon")
	public ResponseEntity<CartResponseDTO> removeCoupon() {
	    String email = SecurityContextHolder.getContext().getAuthentication().getName();
	    User currentUser = userRepo.findByEmail(email).orElseThrow();
	    
	    return ResponseEntity.ok(cartService.removeCoupon(currentUser));
	}
    

}
