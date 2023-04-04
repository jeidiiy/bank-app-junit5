package io.jeidiiy.bankappjunit5.config.jwt;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jeidiiy.bankappjunit5.config.auth.LoginUser;
import io.jeidiiy.bankappjunit5.domain.user.User;
import io.jeidiiy.bankappjunit5.domain.user.UserEnum;

class JwtProcessTests {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Test
	void create_test() {
		//given
		String jwtToken = createToken();
		log.info("JWT TOKEN: {}", jwtToken);

		//then
		assertThat(jwtToken.startsWith(JwtVO.TOKEN_PREFIX)).isTrue();
	}

	@Test
	void verify_test() {
		//given
		String token = createToken();
		String jwtToken = token.replace(JwtVO.TOKEN_PREFIX, "");

		//when
		LoginUser loginUser = JwtProcess.verify(jwtToken);

		//then
		assertThat(loginUser.getUser().getId()).isEqualTo(1L);
		assertThat(loginUser.getUser().getRole()).isEqualTo(UserEnum.CUSTOMER);
	}

	private static String createToken() {
		User user = User.builder().id(1L).role(UserEnum.CUSTOMER).build();
		LoginUser loginUser = new LoginUser(user);
		return JwtProcess.create(loginUser);
	}
}
