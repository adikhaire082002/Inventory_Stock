package com.aditya.inventory.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UserRequestDto {
	
	private String for_admin_login_key_required;
	
	private String name;
	
	private String email;
	
	private String password;
	
	private String[] role;
	
	private Long mobile; 
	
	private String address;

    private String companyName;

    private String gstNo;
	
	
	

}
