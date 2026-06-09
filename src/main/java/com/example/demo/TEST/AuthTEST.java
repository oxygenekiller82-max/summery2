package com.example.demo.TEST;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/test")
public class AuthTEST {

	@GetMapping
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("JWT ??");
    }
}
