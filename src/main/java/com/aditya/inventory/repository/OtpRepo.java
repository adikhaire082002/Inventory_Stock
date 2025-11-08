package com.aditya.inventory.repository;

import com.aditya.inventory.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OtpRepo extends JpaRepository<Otp,Integer> {
    Otp findByEmail(String email);
}
