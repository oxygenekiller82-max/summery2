package com.example.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//DTO : Exceptions global class handler thingy
@Data
@Builder 
@AllArgsConstructor 
@NoArgsConstructor
public class ErrorResponse {
	private int errorStatus;
	private String errorMessage;
	private long errorTimeStamp;

}
