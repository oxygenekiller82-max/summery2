package com.example.demo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity @Setter @Getter 
public class Customization {
	private Long id;
	private String name; // Ex: "extra syrup pump america hell yeah"
	private Double extraPrice; 
	private boolean is_available; 
	
	//relationship with product (=JUICE) 
	//a prudct -> many customization from this pov many to one 
	@ManyToOne 
	@JoinColumn(name="product_id")//many side holds the fk, product_id
	private Product product;
}
