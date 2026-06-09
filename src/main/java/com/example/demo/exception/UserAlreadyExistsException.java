package com.example.demo.exception;

public class UserAlreadyExistsException extends RuntimeException {
	public UserAlreadyExistsException(String message) {
        super(message);//so consructor with message and super -> calls parent consrtuctor with that msg!
    }
}
