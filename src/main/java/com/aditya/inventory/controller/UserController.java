package com.aditya.inventory.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aditya.inventory.dto.BaseResponse;
import com.aditya.inventory.dto.BaseResponseDto;
import com.aditya.inventory.dto.UserRequestDto;
import com.aditya.inventory.dto.UserResponseDto;
import com.aditya.inventory.jwt.JwtUtils;
import com.aditya.inventory.service.UserService;

import io.jsonwebtoken.lang.Arrays;
import jakarta.servlet.http.HttpServletRequest;

@RestController("/User")
public class UserController {



	@Autowired
	private UserService userService;

	@Autowired
	private JwtUtils jwtUtils;


	@DeleteMapping("/Delete")
	public BaseResponse deleteProduct(@RequestParam Integer id, HttpServletRequest request) {
		String jwt = jwtUtils.getJwtFromHeader(request);
		String userNameFromJwtToken = jwtUtils.getUserNameFromJwtToken(jwt);
		UserResponseDto userByEmail = userService.getUserByEmail(userNameFromJwtToken);
		System.out.println(userByEmail.toString());
		UserResponseDto userById = userService.getUserById(id);
		System.out.println(userById.toString());

		if (userByEmail.getMobile() == userById.getMobile() || Arrays.asList(userByEmail.getRole()).contains("Admin")) {
			userService.deleteProduct(id);

			return new BaseResponse(HttpStatus.GONE, "User Deleted successfully ", new Date());

		}
		return new BaseResponse(HttpStatus.UNAUTHORIZED, "You cant delete another user account ", new Date());

	}
	
	@PutMapping("/Update")
	public BaseResponseDto updateProduct(@RequestBody UserRequestDto userRequestDto,HttpServletRequest request) {
		String jwt = jwtUtils.getJwtFromHeader(request);
		String userNameFromJwtToken = jwtUtils.getUserNameFromJwtToken(jwt);
		UserResponseDto userByEmail = userService.getUserByEmail(userNameFromJwtToken);
		UserResponseDto updateUser = userService.updateUser(userRequestDto,userByEmail);
		return new BaseResponseDto(HttpStatus.OK, "User update successfully",updateUser, new Date());
	}

}
