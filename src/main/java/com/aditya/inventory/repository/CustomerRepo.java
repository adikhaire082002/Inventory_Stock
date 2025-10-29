package com.aditya.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.aditya.inventory.entity.Customer;

import java.util.List;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, String> {

	@Query("Select a from Customer a where a.user_id = :user_id")
	Customer findByUser_id(String user_id);

    Customer findByEmail(String email);

}
