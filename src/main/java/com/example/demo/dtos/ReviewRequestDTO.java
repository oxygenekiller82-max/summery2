package com.example.demo.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDTO {
	@NotNull(message = "La note est obligatoire")
	@NotNull @Min(1) @Max(5)
    private Integer note;
    private String commentaire;
    
    @NotNull(message = "L'ID du produit est obligatoire")
    private Long productId;
}
