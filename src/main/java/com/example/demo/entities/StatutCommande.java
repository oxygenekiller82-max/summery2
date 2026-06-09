package com.example.demo.entities;

public enum StatutCommande {
	PENDING, //= waiting for PAYMENT! 
    PAID, // then we go prepare the juice! LEMONAAAADE
    PREPARING, // no more processing :( JUICE BEING MADE NOW :)
    READY,
    DELIVERED,
    CANCELLED
}
