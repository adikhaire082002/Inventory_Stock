package com.aditya.inventory.service;

public interface EmailService {

    public void sendMail(String to, String subject, String content);
}
