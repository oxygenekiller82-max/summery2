package com.example.demo.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.Address;
import com.example.demo.entities.User;
import com.example.demo.repositories.AddressRepository;
import com.example.demo.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

	private final AddressRepository addressRepo;
	private final UserRepository userRepo;
	
	//i wonder why i haven't made this a helper already..
	
	private User getCurrentAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
	
	//GET /api/addresses: all addtresses for the current user 
	@GetMapping
    public ResponseEntity<List<Address>> getMyAddresses() {
		User currentUser = getCurrentAuthenticatedUser();
		
        List<Address> addresses = addressRepo.findByUser(currentUser);
        return ResponseEntity.ok(addresses);
    }
	
	//POST: add a new user address api/addresses
	
	@PostMapping
    public ResponseEntity<Address> addAddress(@RequestBody Address address) {
		User currentUser = getCurrentAuthenticatedUser();
        address.setUser(currentUser);
        
        //addresse par défaut = First one i guess
        List<Address> existing = addressRepo.findByUser(currentUser);
        if (existing.isEmpty()) {
            address.setPrincipal(true);
        } else {
            address.setPrincipal(false);
        }
        
        Address saved = addressRepo.save(address);
        return ResponseEntity.ok(saved);

	}
}
















