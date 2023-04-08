package com.boilerplate.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Getter
public abstract class ApiException extends ResponseStatusException {

	protected final String message;

	public ApiException(final HttpStatus status, final String message) {
		super(status, message);
		this.message = message;
	}

	@Override
	public String getMessage() {
		return this.message;
	}
}