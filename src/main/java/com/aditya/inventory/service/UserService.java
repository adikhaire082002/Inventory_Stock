package com.aditya.inventory.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aditya.inventory.dto.UserRequestDto;
import com.aditya.inventory.dto.UserResponseDto;
import com.aditya.inventory.entity.Role;
import com.aditya.inventory.entity.User;
import com.aditya.inventory.mapper.UserMapper;
import com.aditya.inventory.repository.RoleRepo;
import com.aditya.inventory.repository.UserRepo;


@Service
public class UserService {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private RoleRepo roleRepo;
	
	
	//---------------------Adding User-----------------//

	public UserResponseDto addUser(UserRequestDto userRequestDto) {
		String roleName = userRequestDto.getRole().trim();

		Optional<Role> optionalRole = roleRepo.findByRole(roleName);
		Role role;
		if (optionalRole.isPresent()) {
			role = optionalRole.get();
		} else {
			role = new Role();
			role.setRole(roleName);
			role = roleRepo.save(role);
		}

		User user = userMapper.toUser(userRequestDto, role);
		user.setCreatedAt(new Date());
		User savedUser = userRepo.save(user);

		return userMapper.toDto(savedUser);
	}

	
	//---------------------------Get------------------------------//
	
	//all
	public List<User> getUsers() {
		List<User> all = userRepo.findAll();
		return all;
	}
}
