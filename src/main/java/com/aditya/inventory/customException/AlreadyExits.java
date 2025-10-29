package com.aditya.inventory.customException;

import io.jsonwebtoken.security.Message;

public class AlreadyExits extends RuntimeException {
    public AlreadyExits(String message) {
        super(message + "Already exists");
    }
}
