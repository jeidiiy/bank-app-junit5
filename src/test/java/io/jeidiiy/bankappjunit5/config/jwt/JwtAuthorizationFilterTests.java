package io.jeidiiy.bankappjunit5.config.jwt;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import io.jeidiiy.bankappjunit5.config.auth.LoginUser;
import io.jeidiiy.bankappjunit5.domain.user.User;
import io.jeidiiy.bankappjunit5.domain.user.UserEnum;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class JwtAuthorizationFilterTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void authorization_customer_test() throws Exception {
		//given
		User user = User.builder().id(1L).role(UserEnum.CUSTOMER).build();
		LoginUser loginUser = new LoginUser(user);
		String jwtToken = JwtProcess.create(loginUser);

		//when
		ResultActions resultActions = mockMvc.perform(get("/api/s/hello/test").header(JwtVO.HEADER, jwtToken));

		//then
		resultActions.andExpect(status().isNotFound());
	}

	@Test
	void unAuthorization_customer_test() throws Exception {
		//given

		//when
		ResultActions resultActions = mockMvc.perform(get("/api/s/hello/test"));

		//then
		resultActions.andExpect(status().isUnauthorized());
	}

	@Test
	void authorization_admin_test() throws Exception {
		//given
		User user = User.builder().id(1L).role(UserEnum.ADMIN).build();
		LoginUser loginUser = new LoginUser(user);
		String jwtToken = JwtProcess.create(loginUser);

		//when
		ResultActions resultActions = mockMvc.perform(get("/api/admin/hello/test").header(JwtVO.HEADER, jwtToken));

		//then
		resultActions.andExpect(status().isNotFound());
	}

	@Test
	void unAuthorization_admin_test() throws Exception {
		//given

		//when
		ResultActions resultActions = mockMvc.perform(get("/api/admin/hello/test"));

		//then
		resultActions.andExpect(status().isUnauthorized());
	}
}
