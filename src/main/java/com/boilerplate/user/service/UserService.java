package com.boilerplate.user.service;

import com.boilerplate.user.dto.UserDto;
import com.boilerplate.user.dto.UserRegisterDto;
import com.boilerplate.user.dto.UserSearchDto;
import com.boilerplate.user.exception.UsernameConflictException;
import com.boilerplate.user.model.User;
import com.boilerplate.user.model.UserRole;
import com.boilerplate.user.repository.UserRepository;
import com.boilerplate.user.repository.UserSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final UserSearchRepository userSearchRepository;

	public Page<UserDto> search(UserSearchDto searchDTO, Pageable pageable) {
		return userSearchRepository.search(searchDTO, pageable)
			.map(UserDto::of);
	}

	public UserDto findByUsername(String username) {
		User user = userRepository.findByUsername(username)
			.orElseThrow(() -> new UsernameNotFoundException(username));
		return UserDto.of(user);
	}

	@Transactional
	public UserDto register(UserRegisterDto registerDTO) {
		checkDuplicateUsername(registerDTO.getUsername());

		User newUser = User.builder()
			.username(registerDTO.getUsername())
			.password(passwordEncoder.encode(registerDTO.getPassword()))
			.name(registerDTO.getName())
			.email(registerDTO.getEmail())
			.phoneNumber(registerDTO.getPhoneNumber())
			.role(UserRole.USER)
			.build();

		userRepository.save(newUser);

		return UserDto.of(newUser);
	}

	private void checkDuplicateUsername(String username) {
		userRepository.findByUsername(username)
			.ifPresent(user -> {
				throw new UsernameConflictException("Username already exists: " + username);
			});
	}
}