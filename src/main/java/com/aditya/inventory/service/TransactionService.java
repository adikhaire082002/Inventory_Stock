package com.aditya.inventory.service;

import java.util.List;

import com.aditya.inventory.entity.TransactionalLog;

public interface TransactionService {
	
	TransactionalLog createLog(TransactionalLog log);
	
	List<TransactionalLog> getlogs();

    List<TransactionalLog> getlogsByDate(String date);
}
