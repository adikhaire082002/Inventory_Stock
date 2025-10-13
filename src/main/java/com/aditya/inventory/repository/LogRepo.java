package com.aditya.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aditya.inventory.entity.TransactionalLog;

@Repository
public interface LogRepo extends JpaRepository<TransactionalLog, Integer> {

}
