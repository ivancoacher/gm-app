package com.jsnjwj.api.advice;

import com.jsnjwj.common.enums.ResponseEnum;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.exception.BusinessException;
import io.jsonwebtoken.SignatureException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
public class ExceptionAdvice {

	@ExceptionHandler(SignatureException.class)
	public ApiResponse signatureAdvice(SignatureException e) {
		ApiResponse response = new ApiResponse();
		response.setCode(ResponseEnum.SIGNATURE_ERROR.getResultCode());
		response.setMessage(ResponseEnum.SIGNATURE_ERROR.getResultMsg());
		return response;
	}

	@ExceptionHandler(BusinessException.class)
	public ApiResponse businessAdvice(BusinessException e) {
		ApiResponse response = new ApiResponse();
		response.setCode(e.getCode());
		response.setMessage(e.getMessage());
		return response;
	}

	@ExceptionHandler(Exception.class)
	public ApiResponse exceptionAdvice(BusinessException e) {
		ApiResponse response = new ApiResponse();
		response.setCode(ResponseEnum.ERROR.getResultCode());
		response.setMessage(e.getMessage());
		return response;
	}

}