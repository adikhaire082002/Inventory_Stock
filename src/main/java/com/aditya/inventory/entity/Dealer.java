package com.aditya.inventory.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Dealer {
	
	@Id
    @GeneratedValue(generator = "custom-id")
    @GenericGenerator(
            name = "custom-id",
            strategy = "com.aditya.inventory.idGererator.CustomIdGenerator"
    )
	private String dealer_id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false,unique = true)
	private String email;
	
	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false,unique=true)
	private Long mobileNo;
	
	@Column(nullable = false)
	private String address;
	
	private boolean status=true;
	
	@Column(nullable = false)
	private Date createdAt;
	
	
	private Date updatedAt;

    @Column(nullable = false)
    private String CompanyName;

    @Column(nullable = false)
    private String GSTNo;

    @OneToMany(mappedBy = "dealer", cascade = {CascadeType.PERSIST, CascadeType.MERGE},orphanRemoval = true)
    private List<Product> products = new ArrayList<>();


    @Column(unique = true)
	private String user_id;

}
