package io.jeidiiy.bankappjunit5.domain.transaction;

import java.util.List;

import javax.persistence.EntityManager;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import io.jeidiiy.bankappjunit5.config.dummy.DummyObject;
import io.jeidiiy.bankappjunit5.domain.account.Account;
import io.jeidiiy.bankappjunit5.domain.account.AccountRepository;
import io.jeidiiy.bankappjunit5.domain.user.User;
import io.jeidiiy.bankappjunit5.domain.user.UserRepository;

@ActiveProfiles("test")
@DataJpaTest
class TransactionRepositoryImplTests extends DummyObject {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private EntityManager em;

	@BeforeEach
	void setUp() {
		autoincrementReset();
		dataSetting();
	}

	@Test
	void findTransaction_all_test() {
		//given
		Long accountId = 1L;

		//when
		List<Transaction> transactionList = transactionRepository.findTransactionList(accountId, "ALL", 0);

		transactionList.forEach((transaction -> {
			log.info("id: {}", transaction.getId());
			log.info("amount: {}", transaction.getAmount());
			log.info("sender: {}", transaction.getSender());
			log.info("receiver: {}", transaction.getReceiver());
			log.info("withdrawAccountBalance: {}", transaction.getWithdrawAccountBalance());
			log.info("depositAccountBalance: {}", transaction.getDepositAccountBalance());
			log.info("balance: {}", transaction.getWithdrawAccount().getBalance());
		}));

		//then
		Assertions.assertThat(transactionList.get(3).getDepositAccountBalance()).isEqualTo(800L);
	}

	@Test
	void dataJpa_test1() {
		List<Transaction> transactions = transactionRepository.findAll();
		transactions.forEach((transaction -> {
			log.info("ID: {}", transaction.getId());
			log.info("Sender: {}", transaction.getSender());
			log.info("Receiver: {}", transaction.getReceiver());
			log.info("Gubun: {}", transaction.getGubun());
		}));
	}

	@Test
	void dataJpa_test2() {
		List<Transaction> transactions = transactionRepository.findAll();
		transactions.forEach((transaction -> {
			log.info("ID: {}", transaction.getId());
			log.info("Sender: {}", transaction.getSender());
			log.info("Receiver: {}", transaction.getReceiver());
			log.info("Gubun: {}", transaction.getGubun());
		}));
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

	private void autoincrementReset() {
		em.createNativeQuery("ALTER TABLE user_tb ALTER COLUMN id RESTART WITH 1").executeUpdate();
		em.createNativeQuery("ALTER TABLE account_tb ALTER COLUMN id RESTART WITH 1").executeUpdate();
		em.createNativeQuery("ALTER TABLE transaction_tb ALTER COLUMN id RESTART WITH 1").executeUpdate();
	}
}
