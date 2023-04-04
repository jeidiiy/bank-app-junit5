package io.jeidiiy.bankappjunit5.web;

import static io.jeidiiy.bankappjunit5.dto.account.AccountReqDto.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jeidiiy.bankappjunit5.config.dummy.DummyObject;
import io.jeidiiy.bankappjunit5.domain.account.AccountRepository;
import io.jeidiiy.bankappjunit5.domain.user.User;
import io.jeidiiy.bankappjunit5.domain.user.UserRepository;

@Sql("classpath:db/teardown.sql")
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class AccountControllerTests extends DummyObject {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AccountRepository accountRepository;

	@BeforeEach
	void init() {
		User test = userRepository.save(newUser("test", "스트테"));
		User toast = userRepository.save(newUser("toast", "스트토"));
		accountRepository.save(newAccount(1111L, test));
		accountRepository.save(newAccount(2222L, toast));
	}

	@WithUserDetails(value = "test", setupBefore = TestExecutionEvent.TEST_EXECUTION)
	@Test
	void deleteAccount_test() throws Exception {
		//given
		Long number = 1111L;

		//when
		ResultActions resultActions = mockMvc.perform(delete("/api/s/account/" + number));

		//then
		resultActions.andExpect(status().isOk());
	}

	// setupBefore=TEST_METHOD (init 메서드 실행 전에 수행됨)
	// setupBefore=TEST_EXECUTION (register_test 메서드 실행 전에 수행됨)
	// DB에서 username으로 조회해서 세션에 담아주는 어노테이션
	@WithUserDetails(value = "test", setupBefore = TestExecutionEvent.TEST_EXECUTION)
	@Test
	void register_test() throws Exception {
		//given
		AccountSaveReqDto accountSaveReqDto = new AccountSaveReqDto();
		accountSaveReqDto.setNumber(9999L);
		accountSaveReqDto.setPassword(1234L);
		String requestBody = objectMapper.writeValueAsString(accountSaveReqDto);

		//when
		ResultActions resultActions = mockMvc.perform(
			post("/api/s/account").content(requestBody).contentType(MediaType.APPLICATION_JSON));

		//then
		resultActions.andExpect(status().isCreated());
	}

}
