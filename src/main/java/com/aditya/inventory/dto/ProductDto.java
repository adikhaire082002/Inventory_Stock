package com.aditya.inventory.dto;

import java.util.Date;
import java.util.List;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductDto {
	
    private String name;
	
	private List<String> categories ;
	
	private String brand;
	
	private String description;
	
	private float price;
	
	private int quantity;
	
    private Date createdAt;
	
	private Date updatedAt;
	
	

}
