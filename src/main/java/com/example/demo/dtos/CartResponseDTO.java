package com.example.demo.dtos;

import java.util.List;

import lombok.Data;

@Data
public class CartResponseDTO {
	private Long id;
    private List<CartItemResponseDTO> items;
    private Double totalCartPrice;
    
    //coupon where???? here ?? ASK PROF 
    
    private String appliedCouponCode; 
    private Double discountAmount = 0.0;
    private Double finalPrice;

}
