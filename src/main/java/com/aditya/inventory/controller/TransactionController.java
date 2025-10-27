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
@RequestMapping("/TransactionLog")
public class TransactionController {
	
	@Autowired
    TransactionService transactionService;

    @PreAuthorize("hasAnyRole('admin')")
    @GetMapping("/admin/getlogs")
    public BaseResponseDto getLogs(){
        List<TransactionalLog> getlogs = transactionService.getlogs();
        if(getlogs.size() < 1 ) {
            return new BaseResponseDto(HttpStatus.NOT_FOUND,"Dealers didnt update any stocks",null,new Date());
        }
        return new BaseResponseDto(HttpStatus.FOUND,"Logs of Updated stocks",getlogs,new Date());
    }

    @PreAuthorize("hasAnyRole('admin')")
    @GetMapping("/admin/getlogsByDate")
    public BaseResponseDto getLogsByDate(@RequestParam String date){
        List<TransactionalLog> getlogs = transactionService.getlogsByDate(date);
        if(getlogs.size() < 1 ) {
            return new BaseResponseDto(HttpStatus.NOT_FOUND,"Dealers didnt update any stocks",null,new Date());
        }
        return new BaseResponseDto(HttpStatus.FOUND,"Logs of Updated stocks with given the date " + date ,getlogs,new Date());
    }

}
