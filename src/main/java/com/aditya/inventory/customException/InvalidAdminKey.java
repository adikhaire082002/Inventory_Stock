package com.aditya.inventory.customException;

public class InvalidAdminKey extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public InvalidAdminKey() {
		super("Invalid Admin key");
	}

}
