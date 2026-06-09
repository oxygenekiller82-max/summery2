package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

//whatt is this config package about ?
//-> GENERAL SPRING BEANS ->NOT strictly secuirt guards tho
//like PasswordEncoder , authentication Manager

//NEEDS TO TALK TO USERREPO! 
//but JwtAuthentification filter -> talks to JwtService

@Configuration
@RequiredArgsConstructor 
public class ApplicationConfig {
	private final UserRepository userRepo; 
	
	@Bean 
	public UserDetailsService myUserDetailsService() {
		//if i give email -> return person in users table!
		//-> to Find a user using email! email is the subject after all 
		return username -> userRepo.findByEmail(username) 
				.orElseThrow(()->new UsernameNotFoundException("User:"+username +" Not Found"));
	}
	
	@Bean 
	public AuthenticationProvider authenticationProvider() {
		//CHECKING PASSWORDS!!
		//Dao= DATA CCESS OBJECT! 
		//User repo = a DAO! 
		// DaoAuthenticationProvider = security OBJECT 
		//-> looks at repo -> sees if user exists
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(myUserDetailsService());
		//fill it up ?
		//-> needs DECRYPTOR! 
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
	
	
	@Bean
	//CONTROLS THE LOGIN process!!
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
        //PREDEFINED! 
        //-> ENTRY POINT FOR VALIDATING credentials ! (mdp email prenom) !!! 
        //INPUT = Authentification object 
        //output= Full object is authentification suceesful 
        //also throws exceptions: BadCredentialsException, LockedException
    }
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); 
        //ENCRYPTS PASSWORD! 
    }

}
