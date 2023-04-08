package com.boilerplate.common.controller;

import com.boilerplate.common.dto.ErrorDto;
import com.boilerplate.common.exception.ApiException;
import java.nio.file.AccessDeniedException;
import javax.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {

	@ExceptionHandler(ApiException.class)
	public ResponseEntity<ErrorDto> handleException(final ApiException exception) {
		return handleExceptionInternal(exception, exception.getStatus());
	}

	@ExceptionHandler({EntityNotFoundException.class, UsernameNotFoundException.class})
	public ResponseEntity<ErrorDto> handleNotFoundException(final RuntimeException exception) {
		return handleExceptionInternal(exception, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ErrorDto> handleBadCredentialsException(final RuntimeException exception) {
		return handleExceptionInternal(exception, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorDto> handleAccessDeniedHandler(final RuntimeException exception) {
		return handleExceptionInternal(exception, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDto> handleException(final Exception exception) {
		return handleExceptionInternal(exception, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private ResponseEntity<ErrorDto> handleExceptionInternal(final Exception exception, HttpStatus status) {
		return ResponseEntity.status(status)
			.body(ErrorDto.builder()
				.statusCode(status.value())
				.error(exception.getClass().getSimpleName())
				.message(exception.getMessage())
				.build());
	}

}