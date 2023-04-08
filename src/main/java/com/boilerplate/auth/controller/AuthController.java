package com.boilerplate.auth.controller;

import com.boilerplate.auth.dto.IssueTokensDto;
import com.boilerplate.auth.dto.LoginRequestDto;
import com.boilerplate.auth.service.AuthService;
import com.boilerplate.common.constant.JwtConstant;
import com.boilerplate.common.utils.CookieUtils;
import com.boilerplate.user.dto.UserDto;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
		Authentication authentication = authService.getAuthentication(loginRequestDto);
		IssueTokensDto result = authService.issueTokens(authentication);

		String accessToken = result.getAccessToken();
		String refreshToken = result.getRefreshToken();

		setAccessTokenToHeader(response, accessToken);
		setRefreshTokenToCookie(response, refreshToken);

		return ResponseEntity.ok(result.getUserDto());
	}

	@PostMapping("/logout")
	public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
		authService.logout(request);

		setAccessTokenToHeader(response, null);
		setRefreshTokenToCookie(response, null);

		return ResponseEntity.ok().build();
	}

	void setAccessTokenToHeader(HttpServletResponse response, String accessToken) {
		response.setHeader(JwtConstant.ACCESS_TOKEN_FIELD, accessToken);
	}

	void setRefreshTokenToCookie(HttpServletResponse response, String refreshToken) {
		response.addCookie(CookieUtils.createRefreshTokenCookie(refreshToken));
	}
}
