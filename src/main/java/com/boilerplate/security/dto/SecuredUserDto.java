package com.boilerplate.security.dto;

import com.boilerplate.user.dto.UserDto;
import com.boilerplate.user.model.User;
import java.util.Arrays;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class SecuredUserDto extends org.springframework.security.core.userdetails.User {

	@Getter
	private final UserDto userDTO;

	public SecuredUserDto(User user) {
		super(
			user.getUsername(),
			user.getPassword(),
			Arrays.asList(new SimpleGrantedAuthority(user.getRole().getAuthority()))
		);
		this.userDTO = UserDto.of(user);
	}
}