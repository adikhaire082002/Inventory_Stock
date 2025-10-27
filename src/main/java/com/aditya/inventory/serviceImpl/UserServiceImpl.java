package com.aditya.inventory.serviceImpl;

import java.util.ArrayList;
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
import com.aditya.inventory.service.UserService;


@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private RoleRepo roleRepo;
	
	
	//---------------------Adding User-----------------//

	public UserResponseDto addUser(UserRequestDto userRequestDto) {

		String roleName = userRequestDto.getRole();

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
	public List<UserResponseDto> getUsers() {
		List<User> all = userRepo.findAll();
		List<UserResponseDto> users = new ArrayList<>();
		for(User user:all) {
			users.add(userMapper.toDto(user));
		}
		return users;
	}


	//ByID
	@Override
	public UserResponseDto getUserById(Integer id) {
		User byId = userRepo.findById(id).get();
		return userMapper.toDto(byId);
	}
	
	//ByEmail
	@Override
	public UserResponseDto getUserByEmail(String userNameFromJwtToken) {
		User byEmail = userRepo.findByEmail(userNameFromJwtToken);
		return userMapper.toDto(byEmail);
	}

	
	//Delete User
	@Override
	public boolean deleteProduct(Integer id) {
		userRepo.deleteById(id);
		return true;
	}


	
    //Update User
	@Override
	public UserResponseDto updateUser(UserRequestDto userRequestDto,UserResponseDto userResponseDto) {
		User byEmail = userRepo.findByEmail(userResponseDto.getEmail());
		User user = userMapper.toUser(userRequestDto, byEmail);
		User save = userRepo.save(user);
		return userMapper.toDto(save);
	}

}
