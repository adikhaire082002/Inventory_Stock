package com.aditya.inventory.controller;

import java.util.Date;

import com.aditya.inventory.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.aditya.inventory.dto.BaseResponse;
import com.aditya.inventory.dto.BaseResponseDto;
import com.aditya.inventory.dto.UserRequestDto;
import com.aditya.inventory.dto.UserResponseDto;
import com.aditya.inventory.jwt.JwtUtils;
import com.aditya.inventory.service.UserService;

import io.jsonwebtoken.lang.Arrays;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/User")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public BaseResponse createUser(@RequestBody UserRequestDto userRequestDto) {
        userService.addUser(userRequestDto);
        return new BaseResponse(HttpStatus.CREATED, "Otp send successfully", new Date());
    }

    @PostMapping("/verify")
    public BaseResponse verifyOtp(@RequestParam int otp,@RequestParam String email) {
        userService.verifyOtp(otp, email);
        return new BaseResponse(HttpStatus.CREATED, "User Verified Successfully", new Date());
    }

	@DeleteMapping("/delete")
	public BaseResponse deleteUser(@RequestParam String id, HttpServletRequest request) {
        userService.deleteUser(id,request);
		return new BaseResponse(HttpStatus.UNAUTHORIZED, "User Deleted ", new Date());

	}
	
	@PatchMapping("/update")
	public BaseResponseDto updateUser(@RequestBody UserRequestDto userRequestDto,HttpServletRequest request) {
		UserResponseDto updateUser = userService.updateUser(userRequestDto,request);
		return new BaseResponseDto(HttpStatus.OK, "User update successfully",updateUser, new Date());
	}

}
