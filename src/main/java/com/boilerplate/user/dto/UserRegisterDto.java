package com.boilerplate.user.dto;

import lombok.Getter;

@Getter
public class UserRegisterDto {
	private String username;
	private String password;
	private String name;
	private String email;
	private String phoneNumber;

	public UserRegisterDto(String username, String password, String name, String email, String phoneNumbeprivate) {
		this.username = username;
		this.password = password;
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumbeprivate;
	}
}
