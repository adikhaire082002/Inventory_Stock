package com.aditya.inventory.serviceImpl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aditya.inventory.entity.TransactionalLog;
import com.aditya.inventory.repository.LogRepo;
import com.aditya.inventory.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {
	
	@Autowired
	private LogRepo logRepo;
	
	
	//-----------------Create Transaction----------------------//
	
	public TransactionalLog createLog(TransactionalLog log) {
		TransactionalLog save = logRepo.save(log);
		return save;
	}


    //----------------Get Transactions---------------------------- */
	
	public List<TransactionalLog> getlogs(){
		List<TransactionalLog> all = logRepo.findAll();
		return all;
	}

    @Override
    public List<TransactionalLog> getlogsByDate(String date) {
           List<TransactionalLog> byCreatedAt = logRepo.findByCreatedAt(date);
        return byCreatedAt ;
    }

}
