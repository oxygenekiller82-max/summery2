package com.example.demo.exception;

public class InvalidRefreshTokenException extends RuntimeException {
	public InvalidRefreshTokenException(String message) {
        super(message);//so consructor with message and super -> calls parent consrtuctor with that msg!
	} 


}