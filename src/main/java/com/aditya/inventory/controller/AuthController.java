package com.aditya.inventory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    private final AuthenticationManager authenticationManager;

	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private UserService userService;
    
	@Autowired
	JwtUtils jwtUtils;
	

    AuthController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/signup")
	public BaseResponse createUser(@RequestBody UserRequestDto userRequestDto) {
        if (userRepo.existsByEmail(userRequestDto.getEmail())) {

            return new BaseResponse(HttpStatus.FOUND,
                    "User aleardy register with " + userRequestDto.getEmail() + " this mail", new Date());
        } else if (userRepo.existsByMobileNo(userRequestDto.getMobile())) {

            return new BaseResponse(HttpStatus.FOUND,
                    "User aleardy register with " + userRequestDto.getMobile() + " this mobile", new Date());
        }
		userService.addUser(userRequestDto);

		return new BaseResponse(HttpStatus.CREATED, "User added successfully", new Date());
	}
    
	//--------------User Sign In----------------------//
	@PostMapping("/signin")
    public BaseResponseDto authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail().toLowerCase(), loginRequest.getPassword()));  //check email and password

        SecurityContextHolder.getContext().setAuthentication(authentication);          //Store aunthenticated user in spring security context
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();          // Gives the user info
        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);             // generate Jwt token for valided user
        LoginResponse response = new LoginResponse(userDetails.getUsername(), jwtToken); // response jwt token with email

		return new BaseResponseDto(HttpStatus.OK,"Login Successfully",response,new Date());
    }

}
