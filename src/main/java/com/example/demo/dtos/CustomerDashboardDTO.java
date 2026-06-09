package com.example.demo.dtos;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerDashboardDTO {

	private List<OrderResponseDTO> currentOrders; // PENDING or SHIPPED
    private List<ReviewResponseDTO> latestReviews;
}
