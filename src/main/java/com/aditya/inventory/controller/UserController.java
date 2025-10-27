package com.aditya.inventory.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aditya.inventory.dto.BaseResponse;
import com.aditya.inventory.dto.BaseResponseDto;
import com.aditya.inventory.dto.UserRequestDto;
import com.aditya.inventory.dto.UserResponseDto;
import com.aditya.inventory.jwt.JwtUtils;
import com.aditya.inventory.repository.UserRepo;
import com.aditya.inventory.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController("/User")
public class UserController {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private UserService userService;

	@Autowired
	private JwtUtils jwtUtils;


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

	@PreAuthorize("hasRole('admin')")
	@GetMapping("/AllUsers")
	public BaseResponseDto getUsers() {
		List<UserResponseDto> users = userService.getUsers();
		return new BaseResponseDto(HttpStatus.FOUND, "All Users Found ", users, new Date());
	}

	@DeleteMapping("/delete")
	public BaseResponse deleteProduct(@RequestParam Integer id, HttpServletRequest request) {
		String jwt = jwtUtils.getJwtFromHeader(request);
		String userNameFromJwtToken = jwtUtils.getUserNameFromJwtToken(jwt);
		UserResponseDto userByEmail = userService.getUserByEmail(userNameFromJwtToken);
		System.out.println(userByEmail.toString());
		UserResponseDto userById = userService.getUserById(id);
		System.out.println(userById.toString());

		if (userByEmail.getMobile() == userById.getMobile() || userByEmail.getRole() == "admin") {
			userService.deleteProduct(id);

			return new BaseResponse(HttpStatus.GONE, "User Deleted successfully ", new Date());

		}
		return new BaseResponse(HttpStatus.UNAUTHORIZED, "You cant delete another user account ", new Date());

	}
	
	@PutMapping("/update")
	public BaseResponseDto updateProduct(@RequestBody UserRequestDto userRequestDto,HttpServletRequest request) {
		String jwt = jwtUtils.getJwtFromHeader(request);
		String userNameFromJwtToken = jwtUtils.getUserNameFromJwtToken(jwt);
		UserResponseDto userByEmail = userService.getUserByEmail(userNameFromJwtToken);
		UserResponseDto updateUser = userService.updateUser(userRequestDto,userByEmail);
		return new BaseResponseDto(HttpStatus.OK, "User update successfully",updateUser, new Date());
	}

}
