package com.example.demo.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.ReviewRequestDTO;
import com.example.demo.dtos.ReviewResponseDTO;
import com.example.demo.entities.User;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ReviewController {
	
	private final ReviewService reviewService;
	private final UserRepository userRepo;
	
	//creation 
	@PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
	public ResponseEntity<ReviewResponseDTO> create(@RequestBody ReviewRequestDTO dto, Principal principal){
		//Principal ?? -> gives EMAIL username of the logged in USER !! 
		User currentUser = userRepo.findByEmail(principal.getName())
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));
		
		return new ResponseEntity<>(reviewService.createReview(dto, currentUser), HttpStatus.CREATED);
	}
	
	//Get reviews -> product -> service filters approve=true 
	@GetMapping("/product/{productId}")
	public ResponseEntity<List<ReviewResponseDTO>> getByProduct(@PathVariable Long productId) {
		return ResponseEntity.ok(reviewService.getReviewsByProduct(productId));
	}
	
	//approve a review , admin pre auth
	@PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> approve(@PathVariable Long id) {
        reviewService.approveReview(id);
        return ResponseEntity.ok().build();
    }
	
	//delete not in cahier de charges.. meh it's okayyy
	@DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
	

}
