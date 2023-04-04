package io.jeidiiy.bankappjunit5.service;

import static io.jeidiiy.bankappjunit5.dto.account.AccountRespDto.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jeidiiy.bankappjunit5.config.dummy.DummyObject;
import io.jeidiiy.bankappjunit5.domain.account.Account;
import io.jeidiiy.bankappjunit5.domain.account.AccountRepository;
import io.jeidiiy.bankappjunit5.domain.transaction.Transaction;
import io.jeidiiy.bankappjunit5.domain.transaction.TransactionRepository;
import io.jeidiiy.bankappjunit5.domain.user.User;
import io.jeidiiy.bankappjunit5.domain.user.UserRepository;
import io.jeidiiy.bankappjunit5.dto.account.AccountReqDto;
import io.jeidiiy.bankappjunit5.handler.ex.CustomApiException;

@ExtendWith(MockitoExtension.class)
class AccountServiceTests extends DummyObject {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@InjectMocks
	private AccountService accountService;
	@Mock
	private UserRepository userRepository;
	@Mock
	private AccountRepository accountRepository;
	@Mock
	private TransactionRepository transactionRepository;

	// Account's balance 확인
	// Transaction's balance 확인
	@Test
	void deposit_test() throws Exception {
		//given
		AccountDepositReqDto depositReqDto = AccountDepositReqDto.builder()
			.number(1111L)
			.amount(100L)
			.gubun("DEPOSIT")
			.tel("01011112222")
			.build();

		// stub 1
		User mockUser = newMockUser(1L, "test", "스트테"); // 실행됨
		Account mockDepositAccount1 = newMockAccount(1L, 1111L, 1000L, mockUser); // 실행됨
		given(accountRepository.findByNumber(any())).willReturn(Optional.of(mockDepositAccount1)); // 실행 안 됨

		// stub 2 (스텁이 진행될 때마다 연관된 객체는 새로 만들어서 주입해야 한다. 실행 중 타이밍이 꼬인다.
		User mockUser2 = newMockUser(1L, "test", "스트테"); // 실행됨
		Account mockDepositAccount2 = newMockAccount(1L, 1111L, 1000L, mockUser2); // 실행됨
		Transaction mockTransaction = newMockDepositTransaction(1L, mockDepositAccount2); // 실행됨
		given(transactionRepository.save(any())).willReturn(mockTransaction); // 실행 안 됨

		//when
		AccountDepositRespDto accountDepositRespDto = accountService.deposit(depositReqDto);
		log.info("mockDepositAccount1.balance = {}", mockDepositAccount1.getBalance());
		log.info("mockDepositAccount2.balance = {}", mockDepositAccount2.getBalance());
		log.info("accountDepositRespDto.balance = {}",
			accountDepositRespDto.getTransactionDto().getDepositAccountBalance());

		//then
		assertThat(accountDepositRespDto.getTransactionDto().getDepositAccountBalance()).isEqualTo(1100L);
	}

	@Test
	void delete_test() throws Exception {
		//given
		Long number = 1111L;
		Long userId = 2L;

		// stub 1
		User mockUser = newMockUser(userId, "test", "스트테");
		Account mockAccount = newMockAccount(1L, number, 1000L, mockUser);
		given(accountRepository.findByNumber(any())).willReturn(Optional.of(mockAccount));

		//when
		accountService.delete(number, userId);

		//then
		assertThatThrownBy(() -> {
			throw new CustomApiException("계좌삭제 실패");
		});
	}

	@Test
	void findByLoginUserId_test() {
		//given
		Long userId = 1L;

		// stub 1
		User mockUser = newMockUser(1L, "test", "스트테");
		given(userRepository.findById(any())).willReturn(Optional.of(mockUser));

		// stub 2
		Account mockAccount1 = newMockAccount(1L, 1234L, 1000L, mockUser);
		Account mockAccount2 = newMockAccount(2L, 5678L, 1000L, mockUser);
		List<Account> accounts = Arrays.asList(mockAccount1, mockAccount2);

		given(accountRepository.findByUser_id(any())).willReturn(accounts);

		//when
		AccountListRespDto accountListRespDto = accountService.findByUserId(mockUser.getId());

		//then
		assertThat(accountListRespDto.getFullName()).isEqualTo("스트테");
		assertThat(accountListRespDto.getAccounts().size()).isEqualTo(2);
	}

	@Test
	void register_test() {
		//given
		AccountReqDto.AccountSaveReqDto accountSaveReqDto = new AccountReqDto.AccountSaveReqDto();
		accountSaveReqDto.setNumber(1234L);
		accountSaveReqDto.setPassword(4567L);

		// stub 1
		User mockUser = newMockUser(1L, "test", "1234");
		when(userRepository.findById(any())).thenReturn(Optional.of(mockUser));

		// stub 2
		when(accountRepository.findByNumber(any())).thenReturn(Optional.empty());

		// stub 3
		Account mockAccount = newMockAccount(1L, 1234L, 1000L, mockUser);
		when(accountRepository.save(any())).thenReturn(mockAccount);

		//when
		AccountSaveRespDto accountSaveRespDto = accountService.register(accountSaveReqDto, 1L);

		//then
		assertThat(accountSaveRespDto.getNumber()).isEqualTo(mockAccount.getNumber());
	}

}
