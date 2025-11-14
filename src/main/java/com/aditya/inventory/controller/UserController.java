package com.aditya.inventory.controller;

import java.io.IOException;
import java.util.Date;

import com.aditya.inventory.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<BaseResponse> createUser(@RequestBody UserRequestDto userRequestDto) {
        userService.addUser(userRequestDto);
        BaseResponse response = new BaseResponse(HttpStatus.OK, "Otp send successfully", new Date());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify")
    public ResponseEntity<BaseResponse> verifyOtp(@RequestParam int otp,@RequestParam String email) {
        userService.verifyOtp(otp, email);
        BaseResponse response = new BaseResponse(HttpStatus.OK, "User Verified Successfully", new Date());
        return ResponseEntity.ok(response);
    }

	@DeleteMapping("/delete")
	public ResponseEntity<BaseResponse> deleteUser(@RequestParam String id, HttpServletRequest request) throws IOException {
        userService.deleteUser(id,request);
        BaseResponse response = new BaseResponse(HttpStatus.OK, "User Deleted ", new Date());
        return ResponseEntity.ok(response);
	}
	
	@PatchMapping("/update")
	public ResponseEntity<BaseResponseDto> updateUser(@RequestBody UserRequestDto userRequestDto,HttpServletRequest request) {
		UserResponseDto updateUser = userService.updateUser(userRequestDto,request);
        BaseResponseDto response = new BaseResponseDto(HttpStatus.OK, "User update successfully",updateUser, new Date());
	    return ResponseEntity.ok(response);
    }

}
