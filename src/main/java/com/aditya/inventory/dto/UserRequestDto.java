package com.aditya.inventory.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {

    private String for_admin_login_key_required;

    @NotBlank(message = "Name should not be blank")
    @Pattern(
            regexp ="^[a-zA-Z\\s]+$",
            message = "Name contains only alphabets"
    )
    private String name;

    @NotBlank(message = "Email should not be blank")
    @Email(message = "Email should be in proper format")
    private String email;

    @Size(min = 8, message = "Minimum length of password is 8")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one digit and one special character"
    )
    private String password;

    @NotNull(message = "Role should not be null")
    @Size(min = 1, message = "Enter at least one role")
    private String[] role;

    @NotNull(message = "Mobile should not be null")
    @Digits(integer = 10, fraction = 0 ,message = "Enter 10 digit number")
    private Long mobile;



    @NotBlank(message = "Address should not be blank")
    @Pattern(
            regexp ="^(?=.*[A-Za-z])[A-Za-z0-9- ,.&]+$",
            message = "Address contains only alphanumeric value"
    )
    private String address;

    private String companyName;

    private String gstNo;
}
