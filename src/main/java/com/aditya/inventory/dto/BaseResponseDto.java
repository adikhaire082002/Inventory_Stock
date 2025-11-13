package com.aditya.inventory.dto;

import java.util.Date;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponseDto{
	
   private HttpStatus httpStatus;
	
	private String message;
	
	private Object data;
	
	private Date timeStamp;

}
