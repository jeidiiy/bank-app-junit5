package io.jeidiiy.bankappjunit5.web;

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
class TransactionControllerTests extends DummyObject {

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
	void setUp() {
		dataSetting();
		em.clear();
	}

	@WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
	@Test
	void findTransactionList_test() throws Exception {
		//given
		Long number = 1111L;
		String gubun = "ALL";
		String page = "0";

		//when
		ResultActions resultActions = mockMvc.perform(get("/api/s/account/" + number + "/transaction")
			.param("gubun", gubun).param("page", page));
		String responseBody = resultActions.andReturn().getResponse().getContentAsString();
		log.info("responseBody: {}", responseBody);

		//then
		resultActions.andExpect(jsonPath("$.data.transactionDtos[0].balance").value(900L));
		resultActions.andExpect(jsonPath("$.data.transactionDtos[1].balance").value(800L));
		resultActions.andExpect(jsonPath("$.data.transactionDtos[2].balance").value(700L));
		resultActions.andExpect(jsonPath("$.data.transactionDtos[3].balance").value(800L));
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
