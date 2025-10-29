package com.aditya.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.aditya.inventory.entity.Dealer;

@Repository
public interface DealerRepo extends JpaRepository<Dealer, Integer> {

	@Query("Select a from Dealer a where a.user_id = :user_id")
	Dealer findByUser_id(Integer user_id);

}
