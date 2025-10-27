package com.aditya.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aditya.inventory.entity.User;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {

	boolean existsByEmail(String email);
	boolean existsByMobileNo(Long mobile);

	User findByEmail(String username);
}
