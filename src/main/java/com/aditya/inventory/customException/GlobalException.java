package com.aditya.inventory.customException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;
import java.util.NoSuchElementException;

import javax.naming.AuthenticationException;

import com.aditya.inventory.dto.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.aditya.inventory.dto.BaseResponseDto;

import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalException {

	@ExceptionHandler(AuthenticationException.class )
	public BaseResponseDto handlerAuthenticationException(AuthenticationException ex) {
		return new BaseResponseDto(HttpStatus.UNAUTHORIZED,"Bad Credintials",ex.getMessage(), new Date());
	}
	
	@ExceptionHandler(InternalAuthenticationServiceException.class)
	public BaseResponseDto handlerInternalAuthenticationServiceException(InternalAuthenticationServiceException ex) {
		return new BaseResponseDto(HttpStatus.UNAUTHORIZED,"User not found", ex.getMessage(), new Date());
	}
	
	@ExceptionHandler(ResourceNotFound.class)
	public BaseResponseDto handlerResourceNotFound(ResourceNotFound ex) {
		return new BaseResponseDto(HttpStatus.NOT_FOUND,"Resource Not Fount", ex.getMessage(), new Date());
	}

    @ExceptionHandler(NoResourceFoundException.class)
    public BaseResponseDto handlerNoResourceFoundException(NoResourceFoundException ex) {
        return new BaseResponseDto(HttpStatus.NOT_FOUND,"Resource Not Fount", ex.getMessage(), new Date());
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
	
	@ExceptionHandler(InvalidInput.class)
	public BaseResponseDto handlerInvalidInput(InvalidInput ex) {
		return new BaseResponseDto(HttpStatus.UNAUTHORIZED,"Invalid Input", ex.getMessage(), new Date());
	}
	
	@ExceptionHandler(InvalidAdminKey.class)
	public BaseResponseDto handlerInvalidAdminKey(InvalidAdminKey ex) {
		return new BaseResponseDto(HttpStatus.UNAUTHORIZED,"Enter Valid Admin Key for admin SignUp", ex.getMessage(), new Date());
	}
	
	@ExceptionHandler(InvalidRole.class)
	public BaseResponseDto handlerInvalidRole(InvalidRole ex) {
		return new BaseResponseDto(HttpStatus.UNAUTHORIZED,"Enter Valid Role : Admin/Dealer/Customer", ex.getMessage(), new Date());
	}

    @ExceptionHandler(AlreadyExits.class)
    public BaseResponseDto handlerAlreadyExits(AlreadyExits ex) {
        return new BaseResponseDto(HttpStatus.UNAUTHORIZED,"Add New Entity", ex.getMessage(), new Date());
    }

    @ExceptionHandler(InvalidMobileNumber.class)
    public BaseResponse handlerInvalidMobileNumber(InvalidMobileNumber ex) {
        return new BaseResponse(HttpStatus.BAD_REQUEST,"Enter valid 10 digit mobile number", new Date());
    }

    @ExceptionHandler(InvalidEmail.class)
    public BaseResponse handlerInvalidEmail(InvalidEmail ex) {
        return new BaseResponse(HttpStatus.BAD_REQUEST,"Enter valid Email which conatail @ and .", new Date());
    }

    @ExceptionHandler(InvalidGSTNo.class)
    public BaseResponse handlerInvalidGSTNo(InvalidGSTNo ex) {
        return new BaseResponse(HttpStatus.BAD_REQUEST,"Enter valid GST number \n In this pattern (NN-AAA-AN-NNNNA-NAA)", new Date());
    }

    @ExceptionHandler(InvalidPassword.class)
    public BaseResponse handlerInvalidPassword(InvalidPassword ex) {
        return new BaseResponse(HttpStatus.BAD_REQUEST,"Enter valid password \n Which Contains atleast one \n (A_Z),(a-z),(0-9),(any Special character) \n the length should be 8 characters ", new Date());
    }

    @ExceptionHandler(InsufficientStocks.class)
    public BaseResponse handlerInsufficientStocks(InsufficientStocks ex) {
        return new BaseResponse(HttpStatus.BAD_REQUEST,"The quantiy of product are less in stocks ", new Date());
    }

    @ExceptionHandler(UnverifiedEmaIL.class)
    public BaseResponse handlerUnverifiedEmaIL(UnverifiedEmaIL ex) {
        return new BaseResponse(HttpStatus.BAD_REQUEST,"Verify email with otp first ", new Date());
    }
	
	
	
}
