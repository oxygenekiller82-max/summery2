package com.example.demo.mappers;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.demo.dtos.CartItemResponseDTO;
import com.example.demo.dtos.OrderResponseDTO;
import com.example.demo.entities.Order;
import com.example.demo.entities.OrderItem;

@Component
public class OrderMapper {
	
	//order entity -> OrderResponseDTO
	public OrderResponseDTO toResponseDTO(Order order) {
        if (order == null) return null;
        
        return OrderResponseDTO.builder()
                .orderId(order.getId())
                .orderDate(order.getDateCommande())
                .status(order.getStatut().toString())
                .totalAmount(order.getTotalTTC())
                .items(order.getLignes() != null ? 
                       order.getLignes().stream()
                            .map(this::toCartItemResponseDTO)
                            .collect(Collectors.toList()) : null)
                .build();
	}
	
	
	//helper cartItem  -> cartItemResponseDTO
	private CartItemResponseDTO toCartItemResponseDTO(OrderItem item) {
        if (item == null) return null;

        // Using Builder for CartItemResponseDTO
        return CartItemResponseDTO.builder()
                .id(item.getId())
                .productName(item.getProduct().getNom())
                // variant = null ?
                .variantName(item.getVariant() != null ? item.getVariant().getAttribut() : "Standard")
                .unitPrice(item.getPrixUnitaire())
                .quantity(item.getQuantite())
                // sub total
                .subTotal(item.getPrixUnitaire() * item.getQuantite())
                .build();
    }
}
