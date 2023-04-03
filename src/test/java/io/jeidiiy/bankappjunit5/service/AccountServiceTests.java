package io.jeidiiy.bankappjunit5.service;

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

import io.jeidiiy.bankappjunit5.config.dummy.DummyObject;
import io.jeidiiy.bankappjunit5.domain.account.Account;
import io.jeidiiy.bankappjunit5.domain.account.AccountRepository;
import io.jeidiiy.bankappjunit5.domain.user.User;
import io.jeidiiy.bankappjunit5.domain.user.UserRepository;
import io.jeidiiy.bankappjunit5.dto.account.AccountReqDto;
import io.jeidiiy.bankappjunit5.dto.account.AccountRespDto;
import io.jeidiiy.bankappjunit5.dto.account.AccountRespDto.AccountListRespDto;

@ExtendWith(MockitoExtension.class)
class AccountServiceTests extends DummyObject {

	@InjectMocks
	private AccountService accountService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private AccountRepository accountRepository;

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
		AccountRespDto.AccountSaveRespDto accountSaveRespDto = accountService.register(accountSaveReqDto, 1L);

		//then
		assertThat(accountSaveRespDto.getNumber()).isEqualTo(mockAccount.getNumber());
	}

}
