package com.boilerplate.user.dto;

/**
 * A DTO for the {@link User} entity
 */

import com.boilerplate.user.model.User;
import com.boilerplate.user.model.UserRole;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserDto implements Serializable {

	private Long id;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	private String username;

	private String name;

	private String email;

	private String phoneNumber;

	private UserRole role;

	@Builder
	public UserDto(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, String username, String name, String email, String phoneNumber, UserRole role) {
		this.id = id;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.username = username;
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.role = role;
	}

	public static UserDto of(User entity) {
		return UserDto.builder()
			.id(entity.getId())
			.createdAt(entity.getCreatedAt())
			.updatedAt(entity.getUpdatedAt())
			.username(entity.getUsername())
			.name(entity.getName())
			.email(entity.getEmail())
			.phoneNumber(entity.getPhoneNumber())
			.role(entity.getRole())
			.build();
	}
}