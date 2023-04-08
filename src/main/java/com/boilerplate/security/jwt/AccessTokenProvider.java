package com.boilerplate.security.jwt;

import com.boilerplate.common.constant.JwtConstant;
import java.time.Duration;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("accessTokenProvider")
public class AccessTokenProvider extends JwtProvider {

	public AccessTokenProvider(
		@Value("${app.auth.accessTokenExpirationDuration}") Duration duration,
		@Value("${app.auth.accessTokenSecret}") String secret,
		@Value("${app.auth.accessTokenReissueDuration}") Duration reissueDuration) {
		super(duration, secret, reissueDuration);
	}

	@Override
	protected String loadTokenInternal(HttpServletRequest request) {
		return request.getHeader(JwtConstant.ACCESS_TOKEN_FIELD);
	}

}
