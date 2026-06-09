package com.example.demo.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder 
public class CustomizationRequestDTO {
	@NotBlank 
	private String name;
	private Double extraPrice; 
	private boolean is_available; 
}
