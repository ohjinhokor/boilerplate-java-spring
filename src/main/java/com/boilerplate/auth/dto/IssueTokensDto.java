package com.boilerplate.auth.dto;

import com.boilerplate.user.dto.UserDto;
import lombok.Getter;

@Getter
public class IssueTokensDto {

	private UserDto userDto;
	private String accessToken;
	private String refreshToken;

	public IssueTokensDto(UserDto userDto, String accessToken, String refreshToken) {
		this.userDto = userDto;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}
}
