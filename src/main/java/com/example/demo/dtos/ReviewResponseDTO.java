package com.example.demo.dtos;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Builder @Data
//to send to front end 
public class ReviewResponseDTO {
	private Long id;
    private Integer note;
    private String commentaire;
    private String customerName; 
    private LocalDateTime dateCreation;
    private boolean approuve;
}
