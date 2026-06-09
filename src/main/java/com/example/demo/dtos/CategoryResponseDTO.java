package com.example.demo.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


//-> UI gets this (id + sub cats)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponseDTO {
	private Long id; 
	private String nom;
	private String description;
	private List<CategoryResponseDTO> sousCategories;
}
