package com.example.demo.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminDashboardDTO {
	private Double globalRevenue; //chiffre d'affaires 
    private List<ProductResponseDTO> topProducts;
    private List<SellerResponseDTO> topSellers;
    private List<OrderResponseDTO> recentOrders;
    
}
