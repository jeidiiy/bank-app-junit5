package io.jeidiiy.bankappjunit5.service;

import static io.jeidiiy.bankappjunit5.dto.account.AccountRespDto.*;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.jeidiiy.bankappjunit5.domain.account.Account;
import io.jeidiiy.bankappjunit5.domain.account.AccountRepository;
import io.jeidiiy.bankappjunit5.domain.transaction.Transaction;
import io.jeidiiy.bankappjunit5.domain.transaction.TransactionEnum;
import io.jeidiiy.bankappjunit5.domain.transaction.TransactionRepository;
import io.jeidiiy.bankappjunit5.domain.user.User;
import io.jeidiiy.bankappjunit5.domain.user.UserRepository;
import io.jeidiiy.bankappjunit5.dto.account.AccountReqDto.AccountDepositReqDto;
import io.jeidiiy.bankappjunit5.dto.account.AccountReqDto.AccountSaveReqDto;
import io.jeidiiy.bankappjunit5.handler.ex.CustomApiException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AccountService {
	private final UserRepository userRepository;
	private final AccountRepository accountRepository;
	private final TransactionRepository transactionRepository;

	@Transactional
	public AccountWithdrawRespDto withdraw(AccountWithdrawReqDto accountWithdrawReqDto, Long userId) {
		// 0원 체크
		if (accountWithdrawReqDto.getAmount() <= 0L) {
			throw new CustomApiException("0원 이하의 금액을 입금할 수 없습니다");
		}

		// 출금계좌 확인
		Account withdrawAccountPS = accountRepository.findByNumber(accountWithdrawReqDto.getNumber())
			.orElseThrow(() -> new CustomApiException("계좌를 찾을 수 없습니다."));

		// 출금 소유자 확인(로그인한 사람과 동일한지)
		withdrawAccountPS.checkOwner(userId);

		// 출금계좌 비밀번호 확인
		withdrawAccountPS.checkSamePassword(accountWithdrawReqDto.getPassword());

		// 출금계좌 잔액 확인
		withdrawAccountPS.checkBalance(accountWithdrawReqDto.getAmount());

		// 출금하기
		withdrawAccountPS.withdraw(accountWithdrawReqDto.getAmount());

		// 거래내역 남기기 (내 계좌에서 ATM으로 출금)
		Transaction transaction = Transaction.builder()
			.depositAccount(null)
			.withdrawAccount(withdrawAccountPS)
			.depositAccountBalance(null)
			.withdrawAccountBalance(withdrawAccountPS.getBalance())
			.amount(accountWithdrawReqDto.getAmount())
			.gubun(TransactionEnum.WITHDRAW)
			.sender(accountWithdrawReqDto.getNumber() + "")
			.receiver("ATM")
			.build();

		Transaction transactionPS = transactionRepository.save(transaction);

		// DTO 응답
		return new AccountWithdrawRespDto(withdrawAccountPS, transactionPS);
	}

	@Transactional
	public AccountDepositRespDto deposit(AccountDepositReqDto accountDepositReqDto) {
		// 0원 체크
		if (accountDepositReqDto.getAmount() <= 0L) {
			throw new CustomApiException("0원 이하의 금액을 입금할 수 없습니다");
		}

		// 입금계좌 확인
		Account depositAccountPS = accountRepository.findByNumber(accountDepositReqDto.getNumber())
			.orElseThrow(() -> new CustomApiException("계좌를 찾을 수 없습니다"));

		// 입금
		depositAccountPS.deposit(accountDepositReqDto.getAmount());

		// 거래내역 남기기
		Transaction transaction = Transaction.builder()
			.depositAccount(depositAccountPS)
			.withdrawAccount(null)
			.depositAccountBalance(depositAccountPS.getBalance())
			.withdrawAccountBalance(null)
			.amount(accountDepositReqDto.getAmount())
			.gubun(TransactionEnum.DEPOSIT)
			.sender("ATM")
			.receiver(depositAccountPS.getNumber() + "")
			.tel(accountDepositReqDto.getTel())
			.build();

		Transaction transactionPS = transactionRepository.save(transaction);

		return new AccountDepositRespDto(depositAccountPS, transactionPS);
	}

	@Transactional
	public void delete(Long number, Long userId) {
		// 계좌 확인
		Account accountPS = accountRepository.findByNumber(number)
			.orElseThrow(() -> new CustomApiException("계좌를 찾을 수 없습니다."));
		// 계좌 소유자 확인
		accountPS.checkOwner(userId);

		// 계좌 삭제
		accountRepository.deleteById(accountPS.getId());
	}

	public AccountListRespDto findByUserId(Long userId) {
		User userPS = userRepository.findById(userId).orElseThrow(() -> new CustomApiException("유저를 찾을 수 없습니다"));

		List<Account> accountListPS = accountRepository.findByUser_id(userId);
		return new AccountListRespDto(userPS, accountListPS);
	}

	@Transactional
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

	@Getter
	@Setter
	public static class AccountWithdrawReqDto {
		@NotNull
		@Digits(integer = 4, fraction = 4)
		private Long number;
		@NotNull
		@Digits(integer = 4, fraction = 4)
		private Long password;
		@NotNull
		private Long amount;
		@NotEmpty
		@Pattern(regexp = "^(DEPOSIT)$")
		private String gubun;

	}
}
