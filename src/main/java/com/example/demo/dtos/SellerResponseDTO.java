package com.example.demo.dtos;

import lombok.Builder;
import lombok.Data;

//PURELY for the admin dhasboard that's it 
@Data
@Builder
public class SellerResponseDTO {
	private Long id;
    private String nomBoutique;
    private String logo;
    private Double note;
    private int productCount;
}
