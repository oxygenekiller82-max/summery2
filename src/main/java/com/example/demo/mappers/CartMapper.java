package com.example.demo.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.demo.dtos.CartItemResponseDTO;
import com.example.demo.dtos.CartResponseDTO;
import com.example.demo.entities.Cart;
import com.example.demo.entities.CartItem;

@Component
public class CartMapper {
	//from cart Entity -> cart response DTO 
	public CartResponseDTO toResponseDTO(Cart cart) {
		if (cart==null) return null; 
		
		CartResponseDTO dto = new CartResponseDTO();
		dto.setId(cart.getId());
		
		//each item inside cart entitty -> to CartItemResponse dto 
		List<CartItemResponseDTO> itemDTOs = cart.getLignes().stream()
				.map(this::toCartItemResponseDTO ) // this = current MAPPER instance ofc
				
				.collect(Collectors.toList());
		
		dto.setItems(itemDTOs);
		
		//cart -> total ofc 
		double total = itemDTOs.stream()
				.mapToDouble(CartItemResponseDTO::getSubTotal)
				.sum(); 
		
		dto.setTotalCartPrice(total);
		dto.setAppliedCouponCode(null);
	    dto.setDiscountAmount(0.0);
	    dto.setFinalPrice(total);
		
		return dto;
				
	}
	
	//Cart ITEM -> CartItemResponseDTO
	private CartItemResponseDTO toCartItemResponseDTO (CartItem item) {
		CartItemResponseDTO dto = new CartItemResponseDTO(); 
		//id, productName, qte 
		
		dto.setId(item.getId());
        dto.setProductName(item.getProduct().getNom());
        dto.setQuantity(item.getQuantite());
        
       //price delta = diff right ? 
        double price = item.getProduct().getPrix();
        
        //if variant -> subtraction  i guess ..
        if (item.getVariant() != null) {
            price += item.getVariant().getPrixDelta();
        }
        
        dto.setUnitPrice(price);
        dto.setSubTotal(price * item.getQuantite());
        
        //what if no iamges ? -> fall back!!
        if (item.getProduct().getImages() != null && !item.getProduct().getImages().isEmpty()) {
            dto.setImageUrl(item.getProduct().getImages().get(0));
        }else {
        	dto.setImageUrl("/images/placeholder-product.png");
        }
        
        if (item.getVariant() != null) {
            dto.setVariantName(item.getVariant().getValeur()); // e.g., "Red"
        }
        
        
        return dto;
        
        
		
	}
	

}
