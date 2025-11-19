package com.aditya.inventory.dto;

import java.util.Date;
import java.util.List;


import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductDto {

    @NotBlank(message = "Product name should not be blank")
    @Pattern(
            regexp ="^(?=.*[A-Za-z])[A-Za-z0-9- ,.&]+$",
            message = "Product name contains only alphanumeric value"
    )
    private String name;

    @NotNull(message = "Categories name should not be null")
    @Size(min = 1, message = "At least add one category")
	private List<String> categories ;

    private List<String> images ;


    @NotBlank(message = "Brand should not be blank")
    @Pattern(
            regexp ="^(?=.*[A-Za-z])[A-Za-z0-9- ,.&]+$",
            message = "Brand contains only alphanumeric value"
    )
	private String brand;
	
	private String description;

    @NotNull(message = "Price should not be null")
    @Min(value = 1, message = "Price should not be negative or zero")
	private float price;

    @NotNull(message = "Quantity should not be null")
    @Min(value = 1, message = "Quantity should not be negative or zero")
	private int quantity;

    private Date createdAt;
	
	private Date updatedAt;
	
	

}
