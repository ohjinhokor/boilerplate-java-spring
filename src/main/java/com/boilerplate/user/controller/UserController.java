package com.boilerplate.user.controller;

import com.boilerplate.security.annotation.IsManager;
import com.boilerplate.user.dto.UserDto;
import com.boilerplate.user.dto.UserRegisterDto;
import com.boilerplate.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping("/register")
	public ResponseEntity<UserDto> register(@RequestBody UserRegisterDto userRegisterDto) {
		UserDto registeredUser = userService.register(userRegisterDto);
		return ResponseEntity.ok(registeredUser);
	}

	@IsManager
	@GetMapping("/{username}")
	public ResponseEntity<UserDto> findByUsername(@PathVariable String username) {
		UserDto user = userService.findByUsername(username);
		return ResponseEntity.ok(user);
	}
}
