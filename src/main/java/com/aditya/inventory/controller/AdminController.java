package com.aditya.inventory.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aditya.inventory.dto.BaseResponseDto;
import com.aditya.inventory.dto.UserResponseDto;
import com.aditya.inventory.service.ProductService;
import com.aditya.inventory.service.UserService;

@RestController
@RequestMapping("/Admin")

public class AdminController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	ProductService productService;

	
	@PreAuthorize("hasRole('Admin')")
	@GetMapping("/AllUsers")
	public ResponseEntity<BaseResponseDto> getUsers() {
		List<UserResponseDto> users = userService.getUsers();
        BaseResponseDto response = new BaseResponseDto(HttpStatus.FOUND, "All Users Found ", users, new Date());
        return ResponseEntity.ok(response);
	}
	
	@PreAuthorize("hasRole('Admin')")
	@GetMapping("/AllAdmins")
	public ResponseEntity<BaseResponseDto> getAdmins() {
		List<UserResponseDto> users = userService.getAdmins();
        BaseResponseDto response = new BaseResponseDto(HttpStatus.FOUND, "All Admins Found ", users, new Date());
        return ResponseEntity.ok(response);
	}
		
	@PreAuthorize("hasRole('Admin')")
	@GetMapping("/AllDealers")
	public ResponseEntity<BaseResponseDto> getDealers() {
		List<UserResponseDto> users = userService.getDealers();
        BaseResponseDto response = new BaseResponseDto(HttpStatus.FOUND, "All Dealers Found ", users, new Date());
        return ResponseEntity.ok(response);
	}
	
	@PreAuthorize("hasRole('Admin')")
	@GetMapping("/AllCustomers")
	public ResponseEntity<BaseResponseDto> getCustomers() {
		List<UserResponseDto> users = userService.getCustomers();
        BaseResponseDto response = new BaseResponseDto(HttpStatus.FOUND, "All Customers Found ", users, new Date());
        return ResponseEntity.ok(response);
    }
	
	@PreAuthorize("hasRole('Admin')")
	@GetMapping("/AllUsersSortByRoles")
	public ResponseEntity<BaseResponseDto> getUsersByRoles() {
		 HashMap<String, List<UserResponseDto>> users = userService.getUsersSortByRoles();
        BaseResponseDto response = new BaseResponseDto(HttpStatus.FOUND, "All Customers Found ", users, new Date());
        return ResponseEntity.ok(response);
	}


}
