package com.chrisreal.ws.exceptions;

public class UserServiceException extends RuntimeException {

	private static final long serialVersionUID = -5266115288680264303L;
	
	public UserServiceException(String message) {
		super(message);
	}

}
