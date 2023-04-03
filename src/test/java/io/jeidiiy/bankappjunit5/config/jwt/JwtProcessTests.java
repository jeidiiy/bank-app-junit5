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
		User user = User.builder().id(1L).role(UserEnum.CUSTOMER).build();
		LoginUser loginUser = new LoginUser(user);

		//when
		String jwtToken = JwtProcess.create(loginUser);
		log.info("JWT TOKEN: {}", jwtToken);

		//then
		assertThat(jwtToken.startsWith(JwtVO.TOKEN_PREFIX)).isTrue();
	}

	@Test
	void verify_test() {
		//given
		String tmpJwtToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJiYW5rIiwicm9sZSI6IkNVU1RPTUVSIiwiaWQiOjEsImV4cCI6MTY4MTExNjI0Nn0.c0TR5n6lMyiryBqmvAeDiLQwh5eE452T6fg6Xc4snW-5XHZZ3LE7hnSqBCNaPihtxKp_2MRNn688yUKvl6Byuw";

		//when
		LoginUser loginUser = JwtProcess.verify(tmpJwtToken);

		//then
		assertThat(loginUser.getUser().getId()).isEqualTo(1L);
		assertThat(loginUser.getUser().getRole()).isEqualTo(UserEnum.CUSTOMER);
	}
}
