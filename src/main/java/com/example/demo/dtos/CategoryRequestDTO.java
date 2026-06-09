package com.example.demo.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//never send objects the UI the project said..
//admin creates category -> sends this 
//@Valid -> validation -> notblank
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryRequestDTO {
	@NotBlank(message="The category name cannot be empty")
	//ooh
	private String nom;
	private String description; 
	private Long parentId;
}
