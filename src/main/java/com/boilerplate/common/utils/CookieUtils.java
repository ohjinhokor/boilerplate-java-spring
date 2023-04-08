package com.boilerplate.common.utils;

import com.boilerplate.common.constant.JwtConstant;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.Cookie;
import org.springframework.util.StringUtils;

public class CookieUtils {

	public static String encode(String payload) {
		return URLEncoder.encode(payload, StandardCharsets.UTF_8);
	}

	public static String decode(String payload) {
		return URLDecoder.decode(payload, StandardCharsets.UTF_8);
	}

	public static Cookie createRefreshTokenCookie(String refreshToken) {
		String payload = StringUtils.hasLength(refreshToken) ? encode(JwtConstant.HEADER_PREFIX + refreshToken) : null;
		Cookie cookie = new Cookie(JwtConstant.REFRESH_TOKEN_FIELD, payload);

		cookie.setHttpOnly(true);
		cookie.setPath("/");
		cookie.setMaxAge(60 * 60 * 24 * 31);

		return cookie;
	}
}
