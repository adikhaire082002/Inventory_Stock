package com.aditya.inventory.customException;

public class InvalidMobileNumber extends RuntimeException {
    public InvalidMobileNumber() {
        super("Invalid Mobile Number");
    }
}
