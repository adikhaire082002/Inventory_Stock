package com.aditya.inventory.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.aditya.inventory.controller.ControllerAdvice;
import com.aditya.inventory.customException.InvalidAdminKey;
import com.aditya.inventory.customException.InvalidRole;
import com.aditya.inventory.dto.UserRequestDto;
import com.aditya.inventory.dto.UserResponseDto;
import com.aditya.inventory.entity.Admin;
import com.aditya.inventory.entity.Customer;
import com.aditya.inventory.entity.Dealer;
import com.aditya.inventory.entity.User;
import com.aditya.inventory.mapper.UserMapper;
import com.aditya.inventory.repository.AdminRepo;
import com.aditya.inventory.repository.CustomerRepo;
import com.aditya.inventory.repository.DealerRepo;
import com.aditya.inventory.repository.UserRepo;
import com.aditya.inventory.service.UserService;

import io.jsonwebtoken.lang.Arrays;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private AdminRepo adminRepo;

	@Autowired
	private DealerRepo dealerRepo;

	@Autowired
	private CustomerRepo customerRepo;

	@Value("${adminLoginKey}")
	String adminKey;

	UserServiceImpl(ControllerAdvice controllerAdvice) {
	}

	// ---------------------Adding User-----------------//

	public UserResponseDto addUser(UserRequestDto userRequestDto) {
		
		
		String[] role = userRequestDto.getRole();
		
		if(role.length==0) {
			throw new InvalidRole();
		}
		for (int i = 0; i < role.length; i++) {
			role[i]= initCap(role[i]);
		}
		userRequestDto.setRole(role);

		for (int i = 0; i < userRequestDto.getRole().length; i++) {
			if(!((userRequestDto.getRole()[i]).equals("Admin")|| userRequestDto.getRole()[i].equals("Dealer") || userRequestDto.getRole()[i].equals("Customer"))) {
				throw new InvalidRole();
			}
				
		}
		
		//Checking Admin key for admin Login
		if (Arrays.asList(userRequestDto.getRole()).contains("Admin")) {
			if (!userRequestDto.getFor_admin_login_key_required().equals(adminKey)) {
				throw new InvalidAdminKey();
			}
		}

		User user = userMapper.toUser(userRequestDto);
		user.setCreatedAt(new Date());
		User savedUser = userRepo.save(user);

		String[] role2 = savedUser.getRole();

		for (int i = 0; i < role2.length; i++) {
			switch (role2[i]) {
			case "Admin": {
				Admin admin = userMapper.toAdmin(savedUser);
				adminRepo.save(admin);
				break;
			}
			case "Dealer": {
				Dealer dealer = userMapper.toDealer(savedUser);
				dealerRepo.save(dealer);
				break;
			}

			case "Customer": {
				Customer customer = userMapper.toCustomer(savedUser);
				customerRepo.save(customer);
				break;
			}

			}

		}

		return userMapper.toDto(savedUser);
	}

	// ---------------------------Get------------------------------//

	// all
	public List<UserResponseDto> getUsers() {
		List<User> all = userRepo.findAll();
		List<UserResponseDto> users = new ArrayList<>();
		for (User user : all) {
			users.add(userMapper.toDto(user));
		}
		return users;
	}

	// ByID
	@Override
	public UserResponseDto getUserById(Integer id) {
		User byId = userRepo.findById(id).get();
		return userMapper.toDto(byId);
	}

	// ByEmail
	@Override
	public UserResponseDto getUserByEmail(String userNameFromJwtToken) {
		User byEmail = userRepo.findByEmail(userNameFromJwtToken);
		return userMapper.toDto(byEmail);
	}

	// Delete User
	@Override
	public boolean deleteProduct(Integer id) {
		userRepo.deleteById(id);
		return true;
	}

	// Update User
	@Override
	public UserResponseDto updateUser(UserRequestDto userRequestDto, UserResponseDto userResponseDto) {
		if (Arrays.asList(userRequestDto.getRole()).contains("Admin")) {
			if (!userRequestDto.getFor_admin_login_key_required().equals(adminKey)) {
				throw new InvalidAdminKey();
			}
		}
		User byEmail = userRepo.findByEmail(userResponseDto.getEmail());
		User user = userMapper.toUser(userRequestDto, byEmail);
		user.setUpdatedAt(new Date());
		User savedUser = userRepo.save(user);

		String[] role = savedUser.getRole();
		for (int i = 0; i < role.length; i++) {
			switch (role[i]) {

			case "Admin": {
				Admin byUser_id = adminRepo.findByUser_id(savedUser.getUser_id());
				Admin admin = userMapper.toAdmin(savedUser, byUser_id);
				adminRepo.save(admin);
				break;
			}
			case "Dealer": {
				Dealer byUser_id = dealerRepo.findByUser_id(savedUser.getUser_id());
				Dealer dealer = userMapper.toDealer(savedUser, byUser_id);
				dealerRepo.save(dealer);
				break;
			}

			case "Customer": {
				Customer byUser_id = customerRepo.findByUser_id(savedUser.getUser_id());
				Customer customer = userMapper.toCustomer(savedUser, byUser_id);
				customerRepo.save(customer);
				break;
			}

			}

		}

		return userMapper.toDto(savedUser);
	}
	
	
	//Initial capital method string
	public String initCap(String value) {
		value = value.substring(0,1).toUpperCase()+value.substring(1).toLowerCase();
		return value;
	}

	//All dealers
	@Override
	public List<UserResponseDto> getDealers() {
		List<User> dealers = userRepo.getDealers();
		List<UserResponseDto> users = new ArrayList<>();
		for (User user : dealers) {
			users.add(userMapper.toDto(user));
		}
		return users;
	}

	//All Admins
	@Override
	public List<UserResponseDto> getAdmins() {
		List<User> admins = userRepo.getAdmins();
		List<UserResponseDto> users = new ArrayList<>();
		for (User user : admins) {
			users.add(userMapper.toDto(user));
		}
		return users;
	}

	//All Customers
	@Override
	public List<UserResponseDto> getCustomers() {
		List<User> customers = userRepo.getCustomers();
		List<UserResponseDto> users = new ArrayList<>();
		for (User user : customers) {
			users.add(userMapper.toDto(user));
		}
		return users;
	}

	
	//All user sort by Roles
	@Override
	public HashMap<String,List<UserResponseDto>> getUsersSortByRoles() {
		HashMap<String,List<UserResponseDto> > users = new HashMap<>(); 
		List<UserResponseDto> admins = getAdmins();
		List<UserResponseDto> dealers = getDealers();
		List<UserResponseDto> customers = getCustomers();
		
		users.put("Admins", admins);
		users.put("Dealers", dealers);
		users.put("Customers", customers);
		
		return users;
	}
	
	
	

}
