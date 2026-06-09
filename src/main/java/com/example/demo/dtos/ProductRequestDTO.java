package com.example.demo.dtos;

import java.util.List;

import com.example.demo.entities.Customization;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ProductRequestDTO {
	@NotBlank(message = "Le nom est obligatoire")
    private String nom;
    private String description;
    @Positive(message = "Le prix doit être positif")
    private Double prix;
    private Double prixPromo;
    private Integer stock;
    private List<Long> categoryIds; // Just  IDs from the frontend
    private List<String> images;    // URLs/paths !!!
    private List<VariantRequestDTO> variants; // Nested variants
    
    //added Customization DTOs
    private List<CustomizationRequestDTO> customizations;

}

