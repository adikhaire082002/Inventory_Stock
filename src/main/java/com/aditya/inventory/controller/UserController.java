package com.aditya.inventory.controller;

import java.io.IOException;
import java.util.Date;

import com.aditya.inventory.entity.Otp;
import com.aditya.inventory.repository.UserRepo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
    public ResponseEntity<BaseResponse> createUser(@RequestBody @Valid UserRequestDto userRequestDto) {
        userService.addUser(userRequestDto);
        BaseResponse response = new BaseResponse(HttpStatus.OK, "Otp send successfully", new Date());
        return ResponseEntity.ok(response);
    }


    @PostMapping("/verify")
    public ResponseEntity<BaseResponse> verifyOtp(@RequestBody @Valid Otp otp) {
        userService.verifyOtp(otp.getOtp(), otp.getEmail());
        BaseResponse response = new BaseResponse(HttpStatus.OK, "User Verified Successfully", new Date());
        return ResponseEntity.ok(response);
    }

	@DeleteMapping("/delete")
	public ResponseEntity<BaseResponse> deleteUser(@RequestParam String id, HttpServletRequest request) throws IOException {
        userService.deleteUser(id,request);
        BaseResponse response = new BaseResponse(HttpStatus.OK, "User Deleted ", new Date());
        return ResponseEntity.ok(response);
	}
    @PreAuthorize("hasAnyRole('Dealer','Customer','Admin')")
	@PatchMapping("/update")
	public ResponseEntity<BaseResponseDto> updateUser(@RequestBody UserRequestDto userRequestDto,HttpServletRequest request) {
		UserResponseDto updateUser = userService.updateUser(userRequestDto,request);
        BaseResponseDto response = new BaseResponseDto(HttpStatus.OK, "User update successfully",updateUser, new Date());
	    return ResponseEntity.ok(response);
    }


    @PreAuthorize("hasAnyRole('Dealer','Customer','Admin')")
    @PatchMapping("/changePassword")
    public ResponseEntity<BaseResponse> updateUser(@RequestParam String oldPassword, @RequestParam String newPassword, Authentication  authentication) {
        UserResponseDto updateUser = userService.updatePassword(oldPassword,newPassword,authentication);
        BaseResponse response = new BaseResponse(HttpStatus.OK, "Password update successfully", new Date());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/forgotPassword")
    public ResponseEntity<BaseResponse> updateUser(@RequestParam String email) {
        userService.forgotPassword(email);
        BaseResponse response = new BaseResponse(HttpStatus.OK, "Otp send successfully", new Date());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/resetPassword")
    public ResponseEntity<BaseResponse> updateUser(@RequestParam int otp, @RequestParam String email, @RequestParam String newPassword) {
        userService.resetPassword(otp,email,newPassword);
        BaseResponse response = new BaseResponse(HttpStatus.OK, "Password update successfully", new Date());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/resendOtp")
    public ResponseEntity<BaseResponse> resendOtp(@RequestParam String email) {
        userService.forgotPassword(email);
        BaseResponse response = new BaseResponse(HttpStatus.OK, "Otp send successfully", new Date());
        return ResponseEntity.ok(response);
    }

}
