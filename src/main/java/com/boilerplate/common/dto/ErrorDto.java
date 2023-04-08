package com.boilerplate.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ErrorDto {

	private int statusCode;
	private String error;
	private String message;
}
