package com.example.demo.dtos;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SellerDashboardDTO {
	private Double revenue;
    private Long pendingOrdersCount;
    private List<ProductResponseDTO> lowStockAlerts;
}
