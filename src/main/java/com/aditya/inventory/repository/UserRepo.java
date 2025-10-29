package com.aditya.inventory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.aditya.inventory.entity.User;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {

	boolean existsByEmail(String email);
	boolean existsByMobileNo(Long mobile);

	User findByEmail(String username);
	
	@Query("Select u from User u where u.user_id in (Select d.user_id from Dealer d )")
	List<User> getDealers();
	
	@Query("Select u from User u where u.user_id in (Select a.user_id from Admin a )")
	List<User> getAdmins();
	
	@Query("Select u from User u where u.user_id in (Select c.user_id from Customer c )")
	List<User> getCustomers();
}
