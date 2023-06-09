package io.jeidiiy.bankappjunit5.web;

import static io.jeidiiy.bankappjunit5.dto.account.AccountReqDto.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import io.jeidiiy.bankappjunit5.domain.account.Account;
import io.jeidiiy.bankappjunit5.domain.account.AccountRepository;
import io.jeidiiy.bankappjunit5.domain.transaction.Transaction;
import io.jeidiiy.bankappjunit5.domain.transaction.TransactionRepository;
import io.jeidiiy.bankappjunit5.domain.user.User;
import io.jeidiiy.bankappjunit5.domain.user.UserRepository;

@Sql("classpath:db/teardown.sql")
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class AccountControllerTests extends DummyObject {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private TransactionRepository transactionRepository;
	@Autowired
	private EntityManager em;

	@BeforeEach
	void init() {
		dataSetting();
		em.clear();
	}

	@WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
	@Test
	void findDetailAccount_test() throws Exception {
		//given
		Long number = 1111L;
		String page = "0";

		//when
		ResultActions resultActions = mockMvc.perform(get("/api/s/account/" + number).param("page", page));
		String responseBody = resultActions.andReturn().getResponse().getContentAsString();
		log.info("responseBody: {}", responseBody);

		//then
		resultActions.andExpect(jsonPath("$.data.transactionDtos[0].balance").value(900L));
		resultActions.andExpect(jsonPath("$.data.transactionDtos[1].balance").value(800L));
		resultActions.andExpect(jsonPath("$.data.transactionDtos[2].balance").value(700L));
		resultActions.andExpect(jsonPath("$.data.transactionDtos[3].balance").value(800L));
	}

	@WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
	@Test
	void transferAccount_test() throws Exception {
		//given
		AccountTransferReqDto accountWithdrawReqDto = new AccountTransferReqDto();
		accountWithdrawReqDto.setWithdrawNumber(1111L);
		accountWithdrawReqDto.setDepositNumber(2222L);
		accountWithdrawReqDto.setWithdrawPassword(1234L);
		accountWithdrawReqDto.setAmount(100L);
		accountWithdrawReqDto.setGubun("TRANSFER");

		String requestBody = objectMapper.writeValueAsString(accountWithdrawReqDto);
		log.info("requestBody: {}", requestBody);

		//when
		ResultActions resultActions =
			mockMvc.perform(post("/api/s/account/transfer")
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON));

		//then
		String responseBody = resultActions.andReturn().getResponse().getContentAsString();
		log.info("responseBody: {}", responseBody);
		resultActions.andExpect(status().isCreated());
	}

	@WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
	@Test
	void withdrawAccount_test() throws Exception {
		//given
		AccountWithdrawReqDto accountWithdrawReqDto = new AccountWithdrawReqDto();
		accountWithdrawReqDto.setNumber(1111L);
		accountWithdrawReqDto.setAmount(100L);
		accountWithdrawReqDto.setGubun("WITHDRAW");
		accountWithdrawReqDto.setPassword(1234L);

		String requestBody = objectMapper.writeValueAsString(accountWithdrawReqDto);
		log.info("requestBody: {}", requestBody);

		//when
		ResultActions resultActions =
			mockMvc.perform(post("/api/account/withdraw")
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON));

		//then
		String responseBody = resultActions.andReturn().getResponse().getContentAsString();
		log.info("responseBody: {}", responseBody);
		resultActions.andExpect(status().isCreated());
	}

	@Test
	void depositAccount_test() throws Exception {
		//given
		AccountDepositReqDto depositReqDto = new AccountDepositReqDto(1111L, 100L, "DEPOSIT", "01011112222");

		String requestBody = objectMapper.writeValueAsString(depositReqDto);
		log.info("requestBody: {}", requestBody);

		//when
		ResultActions resultActions =
			mockMvc.perform(post("/api/account/deposit")
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON));

		//then
		String responseBody = resultActions.andReturn().getResponse().getContentAsString();
		log.info("responseBody: {}", responseBody);
		resultActions.andExpect(status().isCreated());
	}

	@WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
	@Test
	void deleteAccount_test() throws Exception {
		//given
		Long number = 4444L;

		//when
		ResultActions resultActions = mockMvc.perform(delete("/api/s/account/" + number));

		//then
		resultActions.andExpect(status().isOk());
	}

	// setupBefore=TEST_METHOD (init 메서드 실행 전에 수행됨)
	// setupBefore=TEST_EXECUTION (register_test 메서드 실행 전에 수행됨)
	// DB에서 username으로 조회해서 세션에 담아주는 어노테이션
	@WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
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

	private void dataSetting() {
		User ssar = userRepository.save(newUser("ssar", "쌀"));
		User cos = userRepository.save(newUser("cos", "코스,"));
		User love = userRepository.save(newUser("love", "러브"));
		User admin = userRepository.save(newUser("admin", "관리자"));

		Account ssarAccount1 = accountRepository.save(newAccount(1111L, ssar));
		Account cosAccount = accountRepository.save(newAccount(2222L, cos));
		Account loveAccount = accountRepository.save(newAccount(3333L, love));
		Account ssarAccount2 = accountRepository.save(newAccount(4444L, ssar));

		Transaction withdrawTransaction1 = transactionRepository
			.save(newWithdrawTransaction(ssarAccount1, accountRepository));
		Transaction depositTransaction1 = transactionRepository
			.save(newDepositTransaction(cosAccount, accountRepository));
		Transaction transferTransaction1 = transactionRepository
			.save(newTransferTransaction(ssarAccount1, cosAccount, accountRepository));
		Transaction transferTransaction2 = transactionRepository
			.save(newTransferTransaction(ssarAccount1, loveAccount, accountRepository));
		Transaction transferTransaction3 = transactionRepository
			.save(newTransferTransaction(cosAccount, ssarAccount1, accountRepository));
	}
}
