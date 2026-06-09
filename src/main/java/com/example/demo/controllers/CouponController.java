package com.example.demo.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.Coupon;
import com.example.demo.services.CouponService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {
	private final CouponService couponService;
	
	//get all coupons 
	@GetMapping //GET /pi/coupons
    public ResponseEntity<List<Coupon>> getAll() {
        return ResponseEntity.ok(couponService.getAllCoupons());
    }
	
	@PostMapping //POST /pi/coupons
    public ResponseEntity<Coupon> create(@RequestBody Coupon coupon) {
        return ResponseEntity.ok(couponService.createCoupon(coupon));
    }
	
	@DeleteMapping("/{id}") // 
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        couponService.deleteCoupon(id);
        return ResponseEntity.noContent().build();
    }
	
	
	@GetMapping("/validate/{code}")
    public ResponseEntity<Coupon> validate(@PathVariable String code) {
        return ResponseEntity.ok(couponService.isCouponValid(code));
    }
	

}
