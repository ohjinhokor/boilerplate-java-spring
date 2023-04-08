package com.boilerplate.user.exception;

import com.boilerplate.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class UsernameConflictException extends ApiException {

	public UsernameConflictException(final String message) {
		super(HttpStatus.CONFLICT, message);
	}
}
