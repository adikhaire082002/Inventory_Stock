package com.aditya.inventory.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aditya.inventory.entity.TransactionalLog;
import com.aditya.inventory.repository.LogRepo;

@Service
public class TransactionService {
	
	@Autowired
	private LogRepo logRepo;
	
	
	//-----------------Create Transaction----------------------//
	
	public TransactionalLog createLog(TransactionalLog log) {
		TransactionalLog save = logRepo.save(log);
		return save;
	}
	
	
	//----------------Get Transactions----------------------------//
	
	public List<TransactionalLog> getlogs(){
		List<TransactionalLog> all = logRepo.findAll();
		return all;
	}

}
