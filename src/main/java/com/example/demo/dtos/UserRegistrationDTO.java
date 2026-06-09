package com.example.demo.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationDTO {
	@NotBlank(message = "First name is required")
	private String prenom; 
	
	@NotBlank (message="Last name is required")
	private String nom;
	
	@NotBlank(message="Email is required.")
	@Email(message = "Email must be valid")
	private String email;
	
	@NotBlank(message = "Password is required")
	@Size(min = 6, message = "Password must be at least 6 characters long")
	private String password;
	
	
	@NotBlank(message = "please do confirm your password")
	@Size(min = 6, message = "Password must be at least 6 characters long")
	private String confirmPassword;
	
}
