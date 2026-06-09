package com.example.demo.mappers;

import org.springframework.stereotype.Component;

import com.example.demo.dtos.ReviewRequestDTO;
import com.example.demo.dtos.ReviewResponseDTO;
import com.example.demo.entities.Product;
import com.example.demo.entities.Review;

@Component
public class ReviewMapper {
	//Reauest dto ->Entity 
	public Review toEntity(ReviewRequestDTO dto) {
        if (dto == null) return null;
        
        //empty:
        Product product = new Product();
        product.setId(dto.getProductId());
        
        return Review.builder()
                .note(dto.getNote())
                .commentaire(dto.getCommentaire())
                .product(product)
                .approuve(false) // not approved first
                .build();
        
        //CUSTOMER -> STILL NULL -> comes from SecutiyContext 
        //what is someobdy sens a reviewRequest with ANY ID .. omg
        //so who wroet the review ? -> extract from secuityContextHolder, JWT
	}

	//Entity -> responseDTO 
	public ReviewResponseDTO toResponseDTO(Review entity) {
        if (entity == null) return null;
        return ReviewResponseDTO.builder()
        		.id(entity.getId())
                .note(entity.getNote())
                .commentaire(entity.getCommentaire())// anonyme ?! pausee
                .customerName(entity.getCustomer() != null ? entity.getCustomer().getNom() : "Anonyme")
                .dateCreation(entity.getDateCreation())
                .approuve(entity.isApprouve())
                .build();
	}
	
}
