package com.aditya.inventory.entity;

import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer user_id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private String email;
	
	@Column(nullable = false)
	private String password;
	
	@ManyToOne
	@JoinColumn( name = "role_id")
	private Role role;
	
	@Column(nullable = false)
	private Long mobileNo;
	
	@Column(nullable = false)
	private String address;
	
	private boolean status=true;
	
	@Column(nullable = false)
	private Date createdAt;
	
	
	private Date updatedAt = null;
	

}
