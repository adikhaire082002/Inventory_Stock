package com.aditya.inventory.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Otp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String email;

    @Digits(integer = 6,fraction = 0, message = "Enter digits only")
    @Min(value = 6 , message = "Enter 6 digit otp")
    @Max(value = 6 , message = "Enter 6 digit otp")
    @NotEmpty(message = "OTP should not be empty")
    @NotNull(message = "OTP should not be null")
    @NotBlank(message = "OTP should not be blank")
    private int otp;
}
