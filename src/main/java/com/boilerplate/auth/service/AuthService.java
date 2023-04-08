package com.boilerplate.auth.service;

import com.boilerplate.auth.dto.IssueTokensDto;
import com.boilerplate.auth.dto.LoginRequestDto;
import com.boilerplate.auth.model.RefreshToken;
import com.boilerplate.auth.repository.RefreshTokenRepository;
import com.boilerplate.security.dto.SecuredUserDto;
import com.boilerplate.security.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import java.time.Duration;
import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

	private final UserDetailsService userDetailsService;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final JwtProvider accessTokenProvider;
	private final JwtProvider refreshTokenProvider;
	private final RefreshTokenRepository refreshTokenRepository;

	public Authentication getAuthentication(String username) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}

	public Authentication getAuthentication(LoginRequestDto loginRequestDto) {
		SecuredUserDto userDetails = (SecuredUserDto) userDetailsService.loadUserByUsername(loginRequestDto.getUsername());

		var authenticationToken = new UsernamePasswordAuthenticationToken(
			userDetails,
			loginRequestDto.getPassword(),
			userDetails.getAuthorities()
		);

		return authenticationManagerBuilder.getObject().authenticate(authenticationToken);
	}

	public IssueTokensDto issueTokens(Authentication authentication) {
		SecuredUserDto userDetails = (SecuredUserDto) authentication.getPrincipal();

		Claims claims = JwtProvider.createClaims(authentication.getName());

		var accessToken = accessTokenProvider.issueToken(claims);
		var refreshToken = refreshTokenProvider.issueToken(claims);

		return new IssueTokensDto(userDetails.getUserDTO(), accessToken, refreshToken);
	}

	public boolean checkExistRefreshTokenOnDB(String refreshToken) {
		return refreshTokenRepository.findByPayload(refreshToken)
			.isPresent();
	}

	@Transactional
	public void logout(HttpServletRequest request) {
		String refreshToken = refreshTokenProvider.loadToken(request);
		refreshTokenRepository.deleteByPayload(refreshToken);
	}

	@Transactional
	public String reissueRefreshToken(String refreshToken) {
		RefreshToken previousRefreshToken = refreshTokenRepository.findByPayload(refreshToken)
			.orElseThrow(IllegalArgumentException::new);

		try {
			checkRefreshTokenReissueAllowed(previousRefreshToken);

			var claims = refreshTokenProvider.getClaims(refreshToken);
			var newRefreshToken = refreshTokenProvider.issueToken(claims);
			previousRefreshToken.renew(newRefreshToken);

			return newRefreshToken;
		} catch (IllegalArgumentException e) {
			refreshTokenRepository.delete(previousRefreshToken);
			throw e;
		}
	}

	void checkRefreshTokenReissueAllowed(RefreshToken refreshToken) {
		LocalDateTime lastUsedAt = refreshToken.getLastUsedAt();
		Duration afterLastReissue = Duration.between(lastUsedAt, LocalDateTime.now());

		if (afterLastReissue.compareTo(getRefreshReissueWatingDuration()) < 0) {
			throw new IllegalStateException("Refresh token reissue is not allowed within 10 minutes");
		}
	}

	Duration getRefreshReissueWatingDuration() {
		var expirationDuration = refreshTokenProvider.getExpirationDuration();
		var reissueDuration = refreshTokenProvider.getReissueDuration();

		return expirationDuration.minus(reissueDuration);
	}
}
