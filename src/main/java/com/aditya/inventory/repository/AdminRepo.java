package com.aditya.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.aditya.inventory.entity.Admin;
@Repository
public interface AdminRepo extends JpaRepository<Admin, Integer>{

	@Query("Select a from Admin a where a.user_id = :user_id")
	Admin findByUser_id(@Param("user_id") Integer user_id);

}
