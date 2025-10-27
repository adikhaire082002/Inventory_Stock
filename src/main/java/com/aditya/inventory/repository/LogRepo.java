package com.aditya.inventory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.aditya.inventory.entity.TransactionalLog;

@Repository
public interface LogRepo extends JpaRepository<TransactionalLog, Integer> {

	@Query("Select l from TransactionalLog l where CAST (l.createdAt as char) like concat(:date,'%')")
	List<TransactionalLog>findByCreatedAt(@Param("date") String date);

}
