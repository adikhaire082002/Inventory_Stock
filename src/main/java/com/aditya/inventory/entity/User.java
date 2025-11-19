package com.aditya.inventory.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.*;
import org.aspectj.bridge.IMessage;
import org.hibernate.annotations.GenericGenerator;

import static org.springframework.security.config.http.MatcherType.regex;

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
    @NotNull(message = "Address should not be null")
    @NotEmpty(message = "Address should not be empty")
    @NotBlank(message = "Address should not be blank")
	private String address;
	
	private boolean status;
	
	@Column(nullable = false)
	private Date createdAt;
	
	
	private Date updatedAt;
	

}
