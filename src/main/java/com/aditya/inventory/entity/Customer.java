package com.aditya.inventory.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
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
public class Customer {
	@Id
    @GeneratedValue(generator = "custom-id")
    @GenericGenerator(
            name = "custom-id",
            strategy = "com.aditya.inventory.idGererator.CustomIdGenerator"
    )
	private String customer_id;
	
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


    @Column(nullable = false)
	private Date createdAt;

    @JsonManagedReference
    @OneToOne(mappedBy = "customer",cascade = CascadeType.ALL)
    private Cart cart;

    private Date updatedAt = null;

    @Column(unique = true)
	private String user_id;

}
