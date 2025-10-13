package com.aditya.inventory.entity;

import java.util.Date;
import java.util.List;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
	
	private List<Integer> dealers;
	

	
}
