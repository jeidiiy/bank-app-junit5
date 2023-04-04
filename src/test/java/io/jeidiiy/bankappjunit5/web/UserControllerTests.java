package io.jeidiiy.bankappjunit5.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jeidiiy.bankappjunit5.config.dummy.DummyObject;
import io.jeidiiy.bankappjunit5.domain.user.UserRepository;
import io.jeidiiy.bankappjunit5.dto.user.UserRequestDto;

@Sql("classpath:db/teardown.sql") // 실행 시점 - @BeforeEach 실행 전
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class UserControllerTests extends DummyObject {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper om;
	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	void init() {
		userRepository.save(newUser("test", "스트테"));
	}

	@DisplayName("회원가입 테스트 성공")
	@Test
	void signup_test_success() throws Exception {
		//given
		UserRequestDto requestDto = new UserRequestDto();
		requestDto.setUsername("toast");
		requestDto.setPassword("1234");
		requestDto.setEmail("test@gmail.com");
		requestDto.setFullName("테스트");

		String requestBody = om.writeValueAsString(requestDto);

		//when
		ResultActions resultActions = mockMvc.perform(
			post("/api/join").content(requestBody).contentType(MediaType.APPLICATION_JSON));

		//then
		resultActions.andExpect(status().isCreated());
	}

	@DisplayName("회원가입 테스트 실패")
	@Test
	void signup_test_fail() throws Exception {
		//given
		UserRequestDto requestDto = new UserRequestDto();
		requestDto.setUsername("test");
		requestDto.setPassword("1234");
		requestDto.setEmail("한글@gmail.com");
		requestDto.setFullName("테스트");

		String requestBody = om.writeValueAsString(requestDto);

		//when
		ResultActions resultActions = mockMvc.perform(
			post("/api/join").content(requestBody).contentType(MediaType.APPLICATION_JSON));

		//then
		resultActions.andExpect(status().isBadRequest());
	}
}
