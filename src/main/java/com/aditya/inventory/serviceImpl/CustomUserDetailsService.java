package com.aditya.inventory.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.aditya.inventory.repository.UserRepo;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepo userRepository;

	public CustomUserDetailsService(UserRepo userRepository) {
		this.userRepository = userRepository;
	}
	
	
	
	//--------------------Load user for authentication/login in user details--------------------------//

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		com.aditya.inventory.entity.User user = userRepository.findByEmail(username);

		return User.builder().username(user.getEmail()).password(user.getPassword()).roles(user.getRole().getRole())
				.build();
	}
}
