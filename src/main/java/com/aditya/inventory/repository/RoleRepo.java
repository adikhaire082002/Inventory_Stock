package com.aditya.inventory.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aditya.inventory.entity.Role;


@Repository
public interface RoleRepo extends JpaRepository<Role, Integer> {

	Optional<Role> findByRole(String role);
	boolean existsByRole(String role);

}
