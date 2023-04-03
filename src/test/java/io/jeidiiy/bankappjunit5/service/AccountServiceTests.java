package io.jeidiiy.bankappjunit5.service;

import static org.mockito.Mockito.*;

import java.util.Optional;

import org.assertj.core.api.Assertions;
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

@ExtendWith(MockitoExtension.class)
class AccountServiceTests extends DummyObject {

	@InjectMocks
	private AccountService accountService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private AccountRepository accountRepository;

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
		Assertions.assertThat(accountSaveRespDto.getNumber()).isEqualTo(mockAccount.getNumber());
	}

}
