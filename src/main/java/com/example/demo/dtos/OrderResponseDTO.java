package com.example.demo.dtos;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDTO {
	private Long orderId;
    private LocalDateTime orderDate;
    private String status; // ENUM StatutCommande
    private Double totalAmount;//Sum  : all subtotals ...
    
    private List<CartItemResponseDTO> items;

}
