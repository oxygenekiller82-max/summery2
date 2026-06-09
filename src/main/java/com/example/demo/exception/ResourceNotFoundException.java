package com.example.demo.exception;

public class ResourceNotFoundException extends  RuntimeException {
	public ResourceNotFoundException(String message) {
        super(message);//so consructor with message and super -> calls parent consrtuctor with that msg!
	} 

}
