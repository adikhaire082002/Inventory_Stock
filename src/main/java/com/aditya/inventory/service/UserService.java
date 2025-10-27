package com.aditya.inventory.service;

import java.util.List;

import com.aditya.inventory.dto.UserRequestDto;
import com.aditya.inventory.dto.UserResponseDto;

public interface UserService {
	
	UserResponseDto addUser(UserRequestDto userRequestDto);
	
	List<UserResponseDto> getUsers() ;

	boolean deleteProduct(Integer id);

	UserResponseDto getUserById(Integer id);

	UserResponseDto getUserByEmail(String userNameFromJwtToken);

	UserResponseDto updateUser(UserRequestDto userRequestDto,UserResponseDto responseDto);

}
