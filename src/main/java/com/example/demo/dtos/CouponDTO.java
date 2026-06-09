package com.example.demo.dtos;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CouponDTO {
	private Long id;
    private String code;
    private String type; 
    private Double valeur;
    private LocalDateTime dateExpiration;
    private Integer usagesMax;
    private Integer usagesActuels;
    private boolean actif;

}
