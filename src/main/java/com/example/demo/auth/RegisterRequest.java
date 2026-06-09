package com.example.demo.auth;

import com.example.demo.entities.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
	    private String prenom;
	    private String nom;
	    private String email;
	    private String password;

	    private UserRole role; 
	    //ATTENTION! USerRole from UI ?
	    
}
