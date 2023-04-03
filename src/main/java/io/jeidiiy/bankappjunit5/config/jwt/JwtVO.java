package io.jeidiiy.bankappjunit5.config.jwt;

public interface JwtVO {
	String SECRET = "SECRET";
	int EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7; // 일주일
	String TOKEN_PREFIX = "Bearer ";
	String HEADER = "Authorization";
}
