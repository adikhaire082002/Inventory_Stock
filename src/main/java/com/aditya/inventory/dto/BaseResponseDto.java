package com.aditya.inventory.dto;

import java.util.Date;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class BaseResponseDto {
	
	
	private HttpStatus httpStatus;
	
	private String meassage;
	
	private Object data;
	
	private Date timeStamp;

}
