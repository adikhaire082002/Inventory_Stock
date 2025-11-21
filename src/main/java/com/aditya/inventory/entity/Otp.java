package com.aditya.inventory.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Otp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

//
//    @NotBlank(message = "Email should not be blank")
//    @Email(message = "Email should be in proper format")
    private String email;

//    @Digits(integer = 6,fraction = 0, message = "Enter 6 digits only")
//    @Min(value = 6 , message = "Enter 6 digit otp")
//    @Max(value = 6 , message = "Enter 6 digit otp")
//    @NotNull(message = "OTP should not be null")
    private int otp;

    private Timestamp created ;

    private Timestamp expiray;
}
