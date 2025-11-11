package com.aditya.inventory.controller;

import java.util.Date;
import java.util.List;

import com.aditya.inventory.entity.TransactionalLog;
import com.aditya.inventory.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<BaseResponseDto> getLogs(){
        List<TransactionalLog> getlogs = transactionService.getlogs();
        BaseResponseDto response =  new BaseResponseDto(HttpStatus.FOUND,"Logs of Updated stocks",getlogs,new Date());
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @GetMapping("/getlogsByDate")
    public ResponseEntity<BaseResponseDto> getLogsByDate(@RequestParam String date){
        List<TransactionalLog> getlogs = transactionService.getlogsByDate(date);
        BaseResponseDto response = new BaseResponseDto(HttpStatus.FOUND,"Logs of Updated stocks with given the date " + date ,getlogs,new Date());
        return ResponseEntity.ok(response);
    }

}
