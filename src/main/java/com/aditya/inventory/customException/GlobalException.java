package com.aditya.inventory.customException;

import java.io.FileNotFoundException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;
import java.util.NoSuchElementException;

import javax.naming.AuthenticationException;

import com.aditya.inventory.dto.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.aditya.inventory.dto.BaseResponseDto;

import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalException {

	@ExceptionHandler(AuthenticationException.class )
	public ResponseEntity<BaseResponseDto> handlerAuthenticationException(AuthenticationException ex) {
        BaseResponseDto response = new BaseResponseDto(HttpStatus.UNAUTHORIZED, "Bad Credintials", ex.getMessage(), new Date());
        return new ResponseEntity<BaseResponseDto>(response,HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(InternalAuthenticationServiceException.class)
	public ResponseEntity<BaseResponseDto> handlerInternalAuthenticationServiceException(InternalAuthenticationServiceException ex) {
		BaseResponseDto response=  new BaseResponseDto(HttpStatus.UNAUTHORIZED,"User not found", ex.getMessage(), new Date());
        return new ResponseEntity<BaseResponseDto>(response,HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(ResourceNotFound.class)
	public ResponseEntity<BaseResponseDto> handlerResourceNotFound(ResourceNotFound ex) {
        BaseResponseDto response=new  BaseResponseDto(HttpStatus.NOT_FOUND,"Resource Not Fount", ex.getMessage(), new Date());
        return new ResponseEntity<BaseResponseDto>(response,HttpStatus.NOT_FOUND);
	}

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<BaseResponseDto> handlerNoResourceFoundException(NoResourceFoundException ex) {
        BaseResponseDto response= new BaseResponseDto(HttpStatus.NOT_FOUND,"Resource Not Fount", ex.getMessage(), new Date());
        return new ResponseEntity<BaseResponseDto>(response,HttpStatus.NOT_FOUND);
    }

	
	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<BaseResponseDto> handlerNoSuchElementException(NoSuchElementException ex) {
        BaseResponseDto response= new BaseResponseDto(HttpStatus.NOT_FOUND,"Product not found", ex.getMessage(), new Date());
        return new ResponseEntity<BaseResponseDto>(response,HttpStatus.NOT_FOUND);
	}
	
	
	@ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
	public ResponseEntity<BaseResponseDto> handlerAuthenticationCredentialsNotFoundException(AuthenticationCredentialsNotFoundException ex) {
        BaseResponseDto response= new BaseResponseDto(HttpStatus.UNAUTHORIZED,"You dont have access of this url", ex.getMessage(), new Date());
        return new ResponseEntity<BaseResponseDto>(response,HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
	public ResponseEntity<BaseResponseDto> handlerSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex) {
        BaseResponseDto response= new BaseResponseDto(HttpStatus.UNAUTHORIZED,"The Email or Mobile is already register", ex.getMessage(), new Date());
        return new ResponseEntity<BaseResponseDto>(response,HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(InvalidInput.class)
	public ResponseEntity<BaseResponseDto> handlerInvalidInput(InvalidInput ex) {
        BaseResponseDto response= new BaseResponseDto(HttpStatus.UNAUTHORIZED,"Invalid Input", ex.getMessage(), new Date());
        return new ResponseEntity<BaseResponseDto>(response,HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(InvalidAdminKey.class)
	public ResponseEntity<BaseResponseDto> handlerInvalidAdminKey(InvalidAdminKey ex) {
        BaseResponseDto response= new BaseResponseDto(HttpStatus.UNAUTHORIZED,"Enter Valid Admin Key for admin SignUp", ex.getMessage(), new Date());
        return new ResponseEntity<BaseResponseDto>(response,HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(InvalidRole.class)
	public ResponseEntity<BaseResponseDto> handlerInvalidRole(InvalidRole ex) {
        BaseResponseDto response= new BaseResponseDto(HttpStatus.UNAUTHORIZED,"Enter Valid Role : Admin/Dealer/Customer", ex.getMessage(), new Date());
        return new ResponseEntity<BaseResponseDto>(response,HttpStatus.UNAUTHORIZED);
	}

    @ExceptionHandler(AlreadyExits.class)
    public ResponseEntity<BaseResponseDto> handlerAlreadyExits(AlreadyExits ex) {
        BaseResponseDto response= new BaseResponseDto(HttpStatus.UNAUTHORIZED,"Add New Entity", ex.getMessage(), new Date());
        return new ResponseEntity<BaseResponseDto>(response,HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidMobileNumber.class)
    public ResponseEntity<BaseResponse> handlerInvalidMobileNumber(InvalidMobileNumber ex) {
        BaseResponse response= new BaseResponse(HttpStatus.BAD_REQUEST,"Enter valid 10 digit mobile number", new Date());
        return new ResponseEntity<BaseResponse>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidEmail.class)
    public ResponseEntity<BaseResponse> handlerInvalidEmail(InvalidEmail ex) {
        BaseResponse response= new BaseResponse(HttpStatus.BAD_REQUEST,"Enter valid Email which contains at least one character @ and .", new Date());
        return new ResponseEntity<BaseResponse>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidGSTNo.class)
    public ResponseEntity<BaseResponse> handlerInvalidGSTNo(InvalidGSTNo ex) {
        BaseResponse response= new BaseResponse(HttpStatus.BAD_REQUEST,"Enter valid GST number In this pattern (NN-AAA-AN-NNNNA-NAA)", new Date());
        return new ResponseEntity<BaseResponse>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidPassword.class)
    public ResponseEntity<BaseResponse> handlerInvalidPassword(InvalidPassword ex) {
        BaseResponse response= new BaseResponse(HttpStatus.BAD_REQUEST,"Enter valid password Which Contains at least one (A_Z),(a-z),(0-9),(any Special character) \n the length should be 8 characters ", new Date());
        return new ResponseEntity<BaseResponse>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InsufficientStocks.class)
    public ResponseEntity<BaseResponse> handlerInsufficientStocks(InsufficientStocks ex) {
        BaseResponse response= new BaseResponse(HttpStatus.BAD_REQUEST,"The quantity of product are less in stocks ", new Date());
        return new ResponseEntity<BaseResponse>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnverifiedEmaIL.class)
    public ResponseEntity<BaseResponse> handlerUnverifiedEmaIL(UnverifiedEmaIL ex) {
        BaseResponse response= new BaseResponse(HttpStatus.BAD_REQUEST,"Verify email with otp first ", new Date());
        return new ResponseEntity<BaseResponse>(response,HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidName.class)
    public ResponseEntity<BaseResponse> handlerInvalidName(InvalidName ex) {
        BaseResponse response= new BaseResponse(HttpStatus.BAD_REQUEST,"Enter valid name", new Date());
        return new ResponseEntity<BaseResponse>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<BaseResponse> handlerFileNotFoundException(FileNotFoundException ex) {
        BaseResponse response= new BaseResponse(HttpStatus.NOT_FOUND,"File not Found", new Date());
        return new ResponseEntity<BaseResponse>(response,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidAddress.class)
    public ResponseEntity<BaseResponse> handlerInvalidAddress(InvalidAddress ex) {
        BaseResponse response= new BaseResponse(HttpStatus.BAD_REQUEST,"Enter valid address", new Date());
        return new ResponseEntity<BaseResponse>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidOTP.class)
    public ResponseEntity<BaseResponse> handlerInvalidOTP(InvalidOTP ex) {
        BaseResponse response= new BaseResponse(HttpStatus.BAD_REQUEST,"Enter valid 6 digit otp", new Date());
        return new ResponseEntity<BaseResponse>(response,HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<BaseResponse> handlerMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        BaseResponse response= new BaseResponse(HttpStatus.BAD_REQUEST,"Enter valid type of  data", new Date());
        return new ResponseEntity<BaseResponse>(response,HttpStatus.BAD_REQUEST);
    }
	
	
	
}
