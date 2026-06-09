package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
	//product -> needs to get its reviews (by id) 
	//reviews -> approved or not ... let'd do both 
	List<Review> findByProductIdAndApprouveTrue(Long productId);
	
	List<Review> findByProductIdAndApprouveFalse(Long productId);
	
	List<Review> findByProductId(Long productId);
	
	List<Review> findByCustomerIdOrderByDateCreationDesc(Long customerId, Pageable pageable);
	
}
