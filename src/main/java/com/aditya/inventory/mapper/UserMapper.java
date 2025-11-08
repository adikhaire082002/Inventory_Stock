package com.aditya.inventory.mapper;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.aditya.inventory.dto.UserRequestDto;
import com.aditya.inventory.dto.UserResponseDto;
import com.aditya.inventory.entity.Admin;
import com.aditya.inventory.entity.Customer;
import com.aditya.inventory.entity.Dealer;
import com.aditya.inventory.entity.User;

@Component
public class UserMapper {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	
	//-----------------UserDto to User---------------//

	public User toUser(UserRequestDto requestDto) {
		User user = new User();
		user.setName(requestDto.getName());
		user.setAddress(requestDto.getAddress());
		user.setEmail(requestDto.getEmail().toLowerCase());
		user.setMobileNo(requestDto.getMobile());
		user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
		user.setRole(requestDto.getRole());

		return user;
	}

	//-----------------User to userDTO-----------------//
	
	public UserResponseDto toDto(User user) {
		UserResponseDto dto = new UserResponseDto();
		dto.setAddress(user.getAddress());
		dto.setName(user.getName());
		dto.setCreatedAt(user.getCreatedAt());
		dto.setUpdatedAt(user.getUpdatedAt());
		dto.setEmail(user.getEmail());
		dto.setMobile(user.getMobileNo());
		dto.setRole(user.getRole());

		return dto;

	}
	
	//-----------------UserDto to User---------------//
	
	public User toUser(UserRequestDto requestDto, User user) {
        if(requestDto.getName()!=null){
            if(requestDto.getName().isEmpty()){
                user.setName(requestDto.getName());
            }
        }
        if(requestDto.getAddress()!=null){
            if(requestDto.getAddress().isEmpty()){
                user.setAddress(requestDto.getAddress());
            }
        }

        if(requestDto.getRole()!=null){
            if(requestDto.getRole().length!=0){
                user.setRole(requestDto.getRole());
            }
        }

        if(requestDto.getEmail()!=null){
            if(!requestDto.getEmail().isEmpty()){
                user.setEmail(requestDto.getEmail().toLowerCase());
            }
        }

        if(requestDto.getMobile()!=null){
            if(requestDto.getMobile()!=0){
                user.setMobileNo(requestDto.getMobile());
            }
        }
        if(requestDto.getPassword()!=null){
            if(!requestDto.getPassword().isEmpty()){
                user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
            }
        }

		return user;
	}
	
	
	//-----------------User to dealer---------------//
	
	public Dealer toDealer(User user) {
		Dealer dealer = new Dealer();
		dealer.setName(user.getName());
		dealer.setAddress(user.getAddress());
		dealer.setEmail(user.getEmail().toLowerCase());
		dealer.setMobileNo(user.getMobileNo());
		dealer.setCreatedAt(user.getCreatedAt());
		dealer.setUpdatedAt(user.getUpdatedAt());
		dealer.setPassword(passwordEncoder.encode(user.getPassword()));
		dealer.setUser_id(user.getUser_id());

		return dealer;
	}
	
	public Dealer toDealer(User user,Dealer dealer) {
		dealer.setName(user.getName());
		dealer.setAddress(user.getAddress());
		dealer.setEmail(user.getEmail().toLowerCase());
		dealer.setMobileNo(user.getMobileNo());
		dealer.setCreatedAt(user.getCreatedAt());
		dealer.setUpdatedAt(user.getUpdatedAt());
		dealer.setPassword(passwordEncoder.encode(user.getPassword()));
		dealer.setUser_id(user.getUser_id());

		return dealer;
	}
	
	
	//-----------------User to Admin---------------//
	
		public Admin toAdmin(User user) {
				Admin admin= new Admin();
			admin.setName(user.getName());
			admin.setAddress(user.getAddress());
			admin.setEmail(user.getEmail().toLowerCase());
			admin.setMobileNo(user.getMobileNo());
			admin.setCreatedAt(user.getCreatedAt());
			admin.setUpdatedAt(user.getUpdatedAt());
			admin.setPassword(passwordEncoder.encode(user.getPassword()));
			admin.setUser_id(user.getUser_id());

			return admin;
		}
		
		public Admin toAdmin(User user, Admin admin) {
		admin.setName(user.getName());
		admin.setAddress(user.getAddress());
		admin.setEmail(user.getEmail().toLowerCase());
		admin.setMobileNo(user.getMobileNo());
		admin.setCreatedAt(user.getCreatedAt());
		admin.setUpdatedAt(user.getUpdatedAt());
		admin.setPassword(passwordEncoder.encode(user.getPassword()));
		admin.setUser_id(user.getUser_id());

		return admin;
	}
		
		
		//-----------------User to Customer---------------//
		
			public Customer toCustomer(User user) {
				Customer customer= new Customer();
				customer.setName(user.getName());
				customer.setAddress(user.getAddress());
				customer.setEmail(user.getEmail());
				customer.setMobileNo(user.getMobileNo());
				customer.setCreatedAt(user.getCreatedAt());
				customer.setUpdatedAt(user.getUpdatedAt());
				customer.setPassword(passwordEncoder.encode(user.getPassword()));
				customer.setUser_id(user.getUser_id());

				return customer;
			}
			
			
			public Customer toCustomer(User user,Customer customer) {
				customer.setName(user.getName());
				customer.setAddress(user.getAddress());
				customer.setEmail(user.getEmail());
				customer.setMobileNo(user.getMobileNo());
				customer.setCreatedAt(user.getCreatedAt());
				customer.setUpdatedAt(user.getUpdatedAt());

				customer.setPassword(passwordEncoder.encode(user.getPassword()));
				customer.setUser_id(user.getUser_id());

				return customer;
			}



}
