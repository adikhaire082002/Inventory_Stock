package com.aditya.inventory.controller;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;
import java.util.NoSuchElementException;

import javax.naming.AuthenticationException;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.aditya.inventory.customException.InvalidAdminKey;
import com.aditya.inventory.customException.InvalidRole;
import com.aditya.inventory.dto.BaseResponseDto;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class ControllerAdvice {

	@ExceptionHandler(AuthenticationException.class )
	public BaseResponseDto handlerAuthenticationException(AuthenticationException ex) {
		return new BaseResponseDto(HttpStatus.UNAUTHORIZED,"Bad Credintials",ex.getMessage(), new Date());
	}
	
	@ExceptionHandler(InternalAuthenticationServiceException.class)
	public BaseResponseDto handlerInternalAuthenticationServiceException(InternalAuthenticationServiceException ex) {
		return new BaseResponseDto(HttpStatus.UNAUTHORIZED,"User not found", ex.getMessage(), new Date());
	}
	
	@ExceptionHandler(EntityNotFoundException.class)
	public BaseResponseDto handlerEntityNotFound(EntityNotFoundException ex) {
		return new BaseResponseDto(HttpStatus.NOT_FOUND,"Product not found", ex.getMessage(), new Date());
	}
	
	
	@ExceptionHandler(NoSuchElementException.class)
	public BaseResponseDto handlerNoSuchElementException(NoSuchElementException ex) {
		return new BaseResponseDto(HttpStatus.NOT_FOUND,"Product not found", ex.getMessage(), new Date());
	}
	
	
	@ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
	public BaseResponseDto handlerAuthenticationCredentialsNotFoundException(AuthenticationCredentialsNotFoundException ex) {
		return new BaseResponseDto(HttpStatus.UNAUTHORIZED,"You dont have access of this url", ex.getMessage(), new Date());
	}
	
	@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
	public BaseResponseDto handlerSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex) {
		return new BaseResponseDto(HttpStatus.UNAUTHORIZED,"The Email or Mobile is already register", ex.getMessage(), new Date());
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public BaseResponseDto handlerIllegalArgumentException(IllegalArgumentException ex) {
		return new BaseResponseDto(HttpStatus.UNAUTHORIZED,"Enter Valid Input", ex.getMessage(), new Date());
	}
	
	@ExceptionHandler(InvalidAdminKey.class)
	public BaseResponseDto handlerInvalidAdminKey(InvalidAdminKey ex) {
		return new BaseResponseDto(HttpStatus.UNAUTHORIZED,"Enter Valid Admin Key for admin SignUp", ex.getMessage(), new Date());
	}
	
	@ExceptionHandler(InvalidRole.class)
	public BaseResponseDto handlerInvalidRole(InvalidRole ex) {
		return new BaseResponseDto(HttpStatus.UNAUTHORIZED,"Enter Valid Role : Admin/Dealer/Customer", ex.getMessage(), new Date());
	}
	
	
	
}
