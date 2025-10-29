package com.aditya.inventory.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserResponseDto {
	
private String name;
	
	private String email;
	
	private String[] role;
	
	private Long mobile; 
	
	private String address;
	
	private boolean status;
	
	private Date createdAt;
	
	private Date updatedAt;
	
	

}
