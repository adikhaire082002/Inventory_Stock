package com.aditya.inventory.controller;

import com.aditya.inventory.service.EmailService;
import com.aditya.inventory.serviceImpl.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.aditya.inventory.dto.BaseResponse;
import com.aditya.inventory.dto.BaseResponseDto;
import com.aditya.inventory.dto.LoginRequest;
import com.aditya.inventory.dto.LoginResponse;
import com.aditya.inventory.dto.UserRequestDto;
import com.aditya.inventory.jwt.JwtUtils;
import com.aditya.inventory.repository.UserRepo;
import com.aditya.inventory.service.UserService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

@RestController
@RequestMapping("/auth")
public class AuthController {


	@Autowired
	private UserService userService;

    @Autowired
    private EmailService emailService;

	//--------------User Sign In----------------------//
	@PostMapping("/signin")
    public BaseResponseDto authenticateUser(@RequestBody LoginRequest loginRequest) {
       LoginResponse response =  userService.authenticateUser(loginRequest);
		return new BaseResponseDto(HttpStatus.OK,"Login Successfully",response,new Date());
    }

    @PostMapping("/sendMail")
    public BaseResponseDto sendMail(@RequestParam String to,String subject,String content) {
        emailService.sendMail(to, subject, content);
        return new BaseResponseDto(HttpStatus.OK,"Mail Sent",null,new Date());

    }

}
