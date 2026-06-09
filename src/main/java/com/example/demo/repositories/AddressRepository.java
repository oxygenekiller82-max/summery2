package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Address;
import com.example.demo.entities.User;

public interface AddressRepository extends JpaRepository<Address, Long> {
	List<Address> findByUser(User user);
	//get all user addresses 

}
