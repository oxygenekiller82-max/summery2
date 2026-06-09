package com.example.demo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder 
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Token {
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id; 
	
	@Column (unique=true)
	private String token; 
	
	@Enumerated(EnumType.STRING)
	//in java -> normal ENUM types ok
	//BUT BD doesn't know what that is -> stock them as STRINGS in the DB nice!
	private TokenType tokenType = TokenType.BEARER; 
	//bearer token -> type d'authentification par who has the token 
	private boolean revoked;
	private boolean expired;
	
	//relation avec user 
	//un user peut avoir plusieurs tokens
	//HOLD ON! user -> logs in from MANY DEVICES -> MORE TOKENS !!!! 
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id")
	private User user;


}








