package com.jsnjwj.user.exception;

public class JwtInvalidException extends RuntimeException {

	public JwtInvalidException(String message) {
		super(message);
	}

}
