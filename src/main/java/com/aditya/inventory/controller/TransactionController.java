package com.aditya.inventory.controller;

import java.util.Date;
import java.util.List;

import com.aditya.inventory.entity.TransactionalLog;
import com.aditya.inventory.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aditya.inventory.dto.BaseResponseDto;


@RestController
@RequestMapping("/transactionLog")
public class TransactionController {
	
	@Autowired
    TransactionService transactionService;

    @PreAuthorize("hasAnyRole('Admin')")
    @GetMapping("/getLogs")
    public BaseResponseDto getLogs(){
        List<TransactionalLog> getlogs = transactionService.getlogs();
        return new BaseResponseDto(HttpStatus.FOUND,"Logs of Updated stocks",getlogs,new Date());
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @GetMapping("/getlogsByDate")
    public BaseResponseDto getLogsByDate(@RequestParam String date){
        List<TransactionalLog> getlogs = transactionService.getlogsByDate(date);
        return new BaseResponseDto(HttpStatus.FOUND,"Logs of Updated stocks with given the date " + date ,getlogs,new Date());
    }

}
