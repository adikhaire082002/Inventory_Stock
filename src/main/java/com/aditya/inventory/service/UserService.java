package com.aditya.inventory.service;

import java.util.HashMap;
import java.util.List;

import com.aditya.inventory.dto.LoginRequest;
import com.aditya.inventory.dto.LoginResponse;
import com.aditya.inventory.dto.UserRequestDto;
import com.aditya.inventory.dto.UserResponseDto;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {
	
	UserResponseDto addUser(UserRequestDto userRequestDto);
	
	List<UserResponseDto> getUsers() ;

	boolean deleteUser(String id, HttpServletRequest request);

	UserResponseDto getUserById(String id);

	UserResponseDto getUserByEmail(String userNameFromJwtToken);

	UserResponseDto updateUser(UserRequestDto userRequestDto,HttpServletRequest request);

	List<UserResponseDto> getDealers();
	
	List<UserResponseDto> getAdmins();
	
	List<UserResponseDto> getCustomers();
	
	HashMap<String,List<UserResponseDto>> getUsersSortByRoles();


    LoginResponse authenticateUser(LoginRequest loginRequest);
}
