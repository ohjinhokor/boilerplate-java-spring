package com.boilerplate.security.filter;

import com.boilerplate.auth.service.AuthService;
import com.boilerplate.common.constant.JwtConstant;
import com.boilerplate.common.utils.CookieUtils;
import com.boilerplate.security.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtProvider accessTokenProvider;
	private final JwtProvider refreshTokenProvider;
	private final AuthService authService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String accessToken = accessTokenProvider.loadToken(request);
		String refreshToken = refreshTokenProvider.loadToken(request);

		if (StringUtils.hasLength(accessToken) || StringUtils.hasLength(refreshToken)) {
			if (accessTokenProvider.shouldReissue(accessToken) && refreshTokenProvider.checkValidity(refreshToken)) {
				if (authService.checkExistRefreshTokenOnDB(refreshToken)) {
					Claims claims = refreshTokenProvider.getClaims(refreshToken);
					String newAccessToken = accessTokenProvider.issueToken(claims);
					response.addHeader(JwtConstant.ACCESS_TOKEN_FIELD, JwtConstant.HEADER_PREFIX + newAccessToken);
					accessToken = newAccessToken;

					if (refreshTokenProvider.shouldReissue(refreshToken)) {
						String newRefreshToken = authService.reissueRefreshToken(refreshToken);
						response.addCookie(CookieUtils.createRefreshTokenCookie(newRefreshToken));
					}
				} else {
					response.addCookie(CookieUtils.createRefreshTokenCookie(null));
				}
			}
			if (accessTokenProvider.checkValidity(accessToken)) {
				Claims claims = accessTokenProvider.getClaims(accessToken);
				Authentication authentication = authService.getAuthentication(claims.getSubject());
				// 권한을 시큐리티 컨텍스트에 넣어줌
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}

		filterChain.doFilter(request, response);
	}
}
