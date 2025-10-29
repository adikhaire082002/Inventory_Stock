package com.aditya.inventory.customException;

public class InvalidRole extends RuntimeException {
	
private static final long serialVersionUID = 1L;
	
	public InvalidRole() {
		super("Invalid Role");
	}

}
