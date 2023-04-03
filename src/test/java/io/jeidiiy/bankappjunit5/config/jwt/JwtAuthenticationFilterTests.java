package io.jeidiiy.bankappjunit5.config.jwt;

import static io.jeidiiy.bankappjunit5.dto.user.UserRequestDto.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jeidiiy.bankappjunit5.config.dummy.DummyObject;
import io.jeidiiy.bankappjunit5.domain.user.UserRepository;

@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class JwtAuthenticationFilterTests extends DummyObject {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	void init() {
		userRepository.save(newUser("test", "스트테"));
	}

	@Test
	void successful_test() throws Exception {
		//given
		LoginReqDto loginReqDto = new LoginReqDto();
		loginReqDto.setUsername("test");
		loginReqDto.setPassword("1234");
		String requestBody = objectMapper.writeValueAsString(loginReqDto);

		//when
		ResultActions resultActions = mockMvc.perform(
			post("/api/login").content(requestBody).contentType(MediaType.APPLICATION_JSON));

		String jwtToken = resultActions.andReturn().getResponse().getHeader(JwtVO.HEADER);

		//then
		resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.username").value("test"));

		assertThat(jwtToken).isNotNull();
		assertThat(jwtToken.startsWith(JwtVO.TOKEN_PREFIX)).isTrue();
	}

	@Test
	void unsuccessful_test() throws Exception {
		//given
		LoginReqDto loginReqDto = new LoginReqDto();
		loginReqDto.setPassword("1234");
		String requestBody = objectMapper.writeValueAsString(loginReqDto);

		//when
		ResultActions resultActions = mockMvc.perform(
			post("/api/login").content(requestBody).contentType(MediaType.APPLICATION_JSON));

		String jwtToken = resultActions.andReturn().getResponse().getHeader(JwtVO.HEADER);

		//then
		resultActions
			.andExpect(status().isUnauthorized());

		assertThat(jwtToken).isNull();
	}
}
