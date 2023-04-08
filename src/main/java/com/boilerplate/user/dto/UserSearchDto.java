package com.boilerplate.user.dto;

import com.boilerplate.common.dto.SearchDto;
import lombok.Getter;

@Getter
public class UserSearchDto implements SearchDto {

	private Long id;
	private String username;
	private String name;
	private String email;
	private String phoneNumber;

	public UserSearchDto(Long id, String username, String name, String email, String phoneNumber) {
		this.id = id;
		this.username = username;
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;
	}
}
