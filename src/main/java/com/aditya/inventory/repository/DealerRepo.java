package com.aditya.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.aditya.inventory.entity.Dealer;

import java.util.List;

@Repository
public interface DealerRepo extends JpaRepository<Dealer, String> {

	@Query("Select a from Dealer a where a.user_id = :user_id")
	Dealer findByUser_id(String user_id);

    Dealer findByEmail(String email);
}
