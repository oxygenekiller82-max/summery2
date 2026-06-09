package com.example.demo.exception;


//what if use tries to logout -> but token doesn't even exist ? boy how did you  even login lmao, this guy must be banned by IP or something lol
public class TokenNotFoundException extends RuntimeException {
	public  TokenNotFoundException(String message) {
        super(message);//so consructor with message and super -> calls parent consrtuctor with that msg!
	}
}
