package com.aditya.inventory.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

	@Id
    @GeneratedValue(generator = "custom-id")
    @GenericGenerator(
            name = "custom-id",
            strategy = "com.aditya.inventory.idGererator.CustomIdGenerator"
    )
	private String user_id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false,unique = true)
	private String email;
	
	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false)
	private String[] role;
	
	@Column(nullable = false,unique=true)
	private Long mobileNo;
	
	@Column(nullable = false)
	private String address;
	
	private boolean status;
	
	@Column(nullable = false)
	private Date createdAt;
	
	
	private Date updatedAt;
	

}
