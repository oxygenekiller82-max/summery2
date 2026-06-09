package com.example.demo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Seller;

public interface SellerRepository extends JpaRepository<Seller, Long> {
	Optional<Seller> findByUserId(Long userId);
	//
}
