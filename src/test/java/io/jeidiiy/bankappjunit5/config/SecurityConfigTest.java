package io.jeidiiy.bankappjunit5.config;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class SecurityConfigTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void authentication_test() throws Exception {
		//given

		//when
		ResultActions resultActions = mockMvc.perform(get("/api/s/hello"));
		int status = resultActions.andReturn().getResponse().getStatus();

		//then
		assertThat(status).isEqualTo(401);
	}

	@Test
	public void authorization_test() throws Exception {
		//given

		//when
		ResultActions resultActions = mockMvc.perform(get("/api/admin/hello"));
		int status = resultActions.andReturn().getResponse().getStatus();

		//then
		assertThat(status).isEqualTo(401);
	}
}
