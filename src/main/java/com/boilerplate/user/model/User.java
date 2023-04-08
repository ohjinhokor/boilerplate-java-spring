package com.boilerplate.user.model;

import com.boilerplate.common.model.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

	@Column(unique = true, nullable = false)
	private String username;
	private String password;
	private String name;
	private String email;
	private String phoneNumber;

	@Enumerated(EnumType.STRING)
	private UserRole role;

	// TODO : Role이 2개 이상인 경우
//	public void addRole(UserRole role) {
//		roles.add(role);
//	}
}
