package com.aditya.inventory.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.aditya.inventory.dto.UserRequestDto;
import com.aditya.inventory.dto.UserResponseDto;
import com.aditya.inventory.entity.Role;
import com.aditya.inventory.entity.User;

@Component
public class UserMapper {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	
	//-----------------UserDto to User---------------//

	public User toUser(UserRequestDto requestDto, Role role) {
		User user = new User();
		user.setName(requestDto.getName());
		user.setAddress(requestDto.getAddress());
		user.setEmail(requestDto.getEmail());
		user.setMobileNo(requestDto.getMobile());
		user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
		user.setRole(role);

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
		dto.setStatus(user.isStatus());
		dto.setMobile(user.getMobileNo());
		dto.setRole(user.getRole().getRole());

		return dto;

	}

}
