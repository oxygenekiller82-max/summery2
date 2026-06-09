package com.example.demo.controllers;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.ProductRequestDTO;
import com.example.demo.dtos.ProductResponseDTO;
import com.example.demo.entities.User;
import com.example.demo.services.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
	
	private final ProductService productService;
	//jsut a remidner Response Entity ? 
	//-> = HTTP ENVELOPE ! body (json data) + status code + THE HEADERS !!
	//<> = type safety, what's inside that envolope :) 
	
	@PostMapping
    @PreAuthorize("hasAnyRole('SELLER','ADMIN')") // ONLY SELLERS ! can access endpoint and create a product
	public ResponseEntity<ProductResponseDTO> createProduct(
			@Valid @RequestBody ProductRequestDTO dto,
			@AuthenticationPrincipal User user) {
		//What the helly is authenticationPrincipal ??
		//->goes to SECURITYCONTEXT ?! -> injects current logged in user 
		//DIRECTLY INTO THE METHOD 
		//-> SO NO NEED TO PARSE THE TOKEN 
		//WAHT ?? TODO
		// why userDeatils needed here... 
		
		//-> JwtFilter validates token ->looks at username -> needs to find user 
		//-> CREATES a UserDetails OBJCET ?? (User) 
		//-> PUTS IT INTO THE SECURITY CONTEXT... W H A T 
	
		ProductResponseDTO response = productService.createProduct(dto, user.getId());
		//User -> implements userDetails -> id from there 

		return new ResponseEntity<>(response, HttpStatus.CREATED);
		//created code = 201
	}
	
	
	// top 10 sellers 
		@GetMapping("/top-selling")
		public ResponseEntity<List<ProductResponseDTO>> getTopSellingProducts(){
			return ResponseEntity.ok(productService.getTopSelling());
		}
		
	
	//next API endpoint -> delete(soft delete) 
	
	@PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> softDelete(@PathVariable Long id) { 
		productService.softDelete(id);
		return ResponseEntity.noContent().build();
		
	}
	
	
	//UPDATE 
	@PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<ProductResponseDTO> updateProduct(
			@PathVariable Long id, @RequestBody ProductRequestDTO dto
			) { 
		return ResponseEntity.ok(productService.update(id,dto));
		
	}
	
	//SEARCH 
	@GetMapping("/search")
	public ResponseEntity<List<ProductResponseDTO>> search(@RequestParam("q") String query){
		//@RequestParam("q") -> THE DATA IS IN THE REQUEST itself ohh after the ?
		return ResponseEntity.ok(productService.search(query));
		
	}
	
	//GET produit + avis +  variants .. 
	@GetMapping("/{id}")
	public ResponseEntity<ProductResponseDTO> getProductDetails(@PathVariable Long id){
		return ResponseEntity.ok(productService.getDetails(id));
	}
	
	//all products... holy 
	@GetMapping //-> jsut GET /products. | cat, min/max price, sellerID, promo 
	public ResponseEntity<Page<ProductResponseDTO>> getAllProducts(
			@RequestParam(required = false) String q, // the search query! q? 
			@RequestParam(required = false) String category,
		    @RequestParam(required = false) Double minPrice,
		    @RequestParam(required = false) Double maxPrice,
		    @RequestParam(required = false) Double minNote,
		    @RequestParam(required = false) Long sellerId,
		    @RequestParam(required = false) Boolean promo,
		    Pageable pageable // the ?page= & size thingy 
			){
		return ResponseEntity.ok(productService.getAllProducts(q,category, minPrice, maxPrice,minNote, sellerId, promo, pageable));
	}
	
	
	
	
	
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
