package io.jeidiiy.bankappjunit5.config.jwt;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import io.jeidiiy.bankappjunit5.config.auth.LoginUser;
import io.jeidiiy.bankappjunit5.domain.user.User;
import io.jeidiiy.bankappjunit5.domain.user.UserEnum;

public class JwtProcess {
	private final Logger log = LoggerFactory.getLogger(getClass());

	// 토큰 생성
	public static String create(LoginUser loginUser) {
		return JwtVO.TOKEN_PREFIX + JWT.create()
			.withSubject("bank")
			.withExpiresAt(new Date(System.currentTimeMillis() + JwtVO.EXPIRATION_TIME))
			.withClaim("id", loginUser.getUser().getId())
			.withClaim("role", loginUser.getUser().getRole() + "")
			.sign(Algorithm.HMAC512(JwtVO.SECRET));
	}

	// 토큰 검증
	public static LoginUser verify(String token) {
		DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(JwtVO.SECRET)).build().verify(token);
		Long id = decodedJWT.getClaim("id").asLong();
		String role = decodedJWT.getClaim("role").asString();
		User user = User.builder().id(id).role(UserEnum.valueOf(role)).build();
		return new LoginUser(user);
	}
}
