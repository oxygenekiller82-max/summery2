package com.example.demo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CartItem {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	//relation avec Cart 
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;
	
	//relation avec produit 
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
	//plusieurs users -> meme produit dans cart dans le meme temps !
	//SINON si one cart <-> one product => ONLY ONE customer will actaully be able to palce
	//for example a shirt in their cart !!! 
	//=> Diffirent cartItem -> point to the one same product done !
	
	private Integer quantite; 
	
	//product -> has variants or no ?
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="variant_id",nullable=true)//variant_id null means has no variants! 
	private ProductVariant variant;
	
	
	
	

}
