package io.jeidiiy.bankappjunit5.service;

import static io.jeidiiy.bankappjunit5.dto.account.AccountRespDto.*;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.jeidiiy.bankappjunit5.domain.account.Account;
import io.jeidiiy.bankappjunit5.domain.account.AccountRepository;
import io.jeidiiy.bankappjunit5.domain.user.User;
import io.jeidiiy.bankappjunit5.domain.user.UserRepository;
import io.jeidiiy.bankappjunit5.dto.account.AccountReqDto.AccountSaveReqDto;
import io.jeidiiy.bankappjunit5.handler.ex.CustomApiException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AccountService {
	private final UserRepository userRepository;
	private final AccountRepository accountRepository;

	public AccountListRespDto findByUserId(Long userId) {
		User userPS = userRepository.findById(userId).orElseThrow(() -> new CustomApiException("유저를 찾을 수 없습니다"));

		List<Account> accountListPS = accountRepository.findByUser_id(userId);
		return new AccountListRespDto(userPS, accountListPS);
	}

	public AccountSaveRespDto register(AccountSaveReqDto accountSaveReqDto, Long userId) {
		// User가 DB에 있는지
		User userPS = userRepository.findById(userId).orElseThrow(() -> new CustomApiException("유저를 찾을 수 없습니다"));
		// 해당 계좌의 중복 여부 체크
		Optional<Account> account = accountRepository.findByNumber(accountSaveReqDto.getNumber());

		if (account.isPresent()) {
			throw new CustomApiException("해당 계좌가 이미 존재합니다.");
		}

		Account accountPS = accountRepository.save(accountSaveReqDto.toEntity(userPS));

		return new AccountSaveRespDto(accountPS);
	}
}
