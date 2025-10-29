package com.aditya.inventory.entity;

import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer product_id;
	
	@Column(nullable = false)
	private String name;

    private List<String> images = new ArrayList<>();
	
	@ManyToMany
	@JoinTable(
	        name = "product_categories",
	        joinColumns = @JoinColumn(name = "product_id"), 
	        inverseJoinColumns = @JoinColumn(name = "category_id")
	    )
	private List<Category> categories ;
	
	@Column(nullable = false)
	private String brand;
	
	@Column(nullable = false)
	private String description;
	
	@Column(nullable = false)
	private float price;
	
	private int quantity;
	
	private int minStockLevel = 10;
	
	private Date createdAt;
	
	private Date updatedAt;

   @ManyToOne(optional = false,fetch = FetchType.LAZY)
   @JoinColumn(name = "dealer_id", nullable = false)
	private Dealer dealer;
	

	
}
