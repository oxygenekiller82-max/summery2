package com.example.demo.dtos;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class CustomizationResponseDTO {
	private Long id;
	private String name;
	private Double extraPrice; 
	private boolean is_available; 

}
