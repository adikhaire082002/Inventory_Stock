package com.aditya.inventory.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
	

	
	@Column(nullable = false)
	private Date createdAt;
	
	
	private Date updatedAt;

    @Column(nullable = false)
//    @NotBlank(message = "Company name should not be blank")
//    @NotNull(message = "Company name should not be null")
//    @NotEmpty(message = "Company name should not be empty")
    private String CompanyName;

    @Column(nullable = false)
//    @NotBlank(message = "GST No. should not be blank")
//    @NotNull(message = "GST No. should not be null")
//    @NotEmpty(message = "GST No. should not be empty")
//    @Pattern(
//            regexp = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$",
//            message = "Enter valid gst number in format - 00AAAAA0000A{0/A}{Z}{0/A}, 0= Any number, A=any alphabet"
//    )
    private String GSTNo;

    @OneToMany(mappedBy = "dealer", cascade = {CascadeType.PERSIST, CascadeType.MERGE},orphanRemoval = true)
    private List<Product> products = new ArrayList<>();


    @Column(unique = true)
	private String user_id;

}
