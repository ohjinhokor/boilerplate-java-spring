package com.boilerplate.security.jwt;

import com.boilerplate.common.constant.JwtConstant;
import com.boilerplate.common.utils.CookieUtils;
import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("refreshTokenProvider")
public class RefreshTokenProvider extends JwtProvider {

	public RefreshTokenProvider(
		@Value("${app.auth.refreshTokenExpirationDuration}") Duration duration,
		@Value("${app.auth.refreshTokenSecret}") String secret,
		@Value("${app.auth.refreshTokenReissueDuration}") Duration reissueDuration) {
		super(duration, secret, reissueDuration);
	}

	@Override
	protected String loadTokenInternal(HttpServletRequest request) {
		var refreshToken = Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
			.filter(cookie -> cookie.getName().equals(JwtConstant.REFRESH_TOKEN_FIELD))
			.findFirst();

		if (refreshToken.isPresent()) {
			String payload = refreshToken.get().getValue();
			return CookieUtils.decode(payload);
		}
		return null;
	}
}