package com.example.demo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entities.Token;

public interface TokenRepository extends JpaRepository<Token, Long> {

	@Query(value="""
			select token from Token token inner join User user\s
			on token.user.id = user.id\s
			where user.id = :id and (token.expired =false or token.revoked=false)\s
			"""
	)
	//JPA doesn't do joins.. 
	//-> Find all tokens from this user that are not expired and not revoked
	//inner join -> links BOTH Token and User table
	//-> Filters tokens by USER ID
	//:id = "named parameter" -> injected where u give id :) 
	List<Token> findAllValidTokenByUser(Long id);
	
	Optional <Token> findByToken(String token);
	
}
