package com.example.demo.auth;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import com.example.demo.exception.TokenNotFoundException;
import com.example.demo.repositories.TokenRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

//LOGOUT = not a controller method..
//-> Spring calls this SERVICE automatically 

@Service 
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {
	private final TokenRepository tokenRepository;

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response,
			@Nullable Authentication authentication) {
		final String authHeader=request.getHeader("Authorization");
		final String jwt; 
		
		//what if no token -> skip 
		if(authHeader==null || !authHeader.startsWith("Bearer")) {
			return;
		}
		//else subtract, find token and invalidate token 
		jwt = authHeader.substring(7).trim();
		var token=tokenRepository.findByToken(jwt)
				.orElseThrow(() -> new TokenNotFoundException("Token not found."));
		
		//what if for some reason it doesn't exist lol so yh.  
		
		if (token!=null) {
			token.setExpired(true);
			token.setRevoked(true);
			tokenRepository.save(token);// save reboked and expired token now
			
			//APPARENTLY need to clear the security context !!!
			//kinda like wiping current session ?
			SecurityContextHolder.clearContext();
		}
	}

}
