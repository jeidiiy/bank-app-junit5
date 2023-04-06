package io.jeidiiy.bankappjunit5.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.jeidiiy.bankappjunit5.domain.account.Account;
import io.jeidiiy.bankappjunit5.domain.account.AccountRepository;
import io.jeidiiy.bankappjunit5.domain.transaction.Transaction;
import io.jeidiiy.bankappjunit5.domain.transaction.TransactionRepository;
import io.jeidiiy.bankappjunit5.dto.transaction.TransactionListRespDto;
import io.jeidiiy.bankappjunit5.handler.ex.CustomApiException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TransactionService {
	private final TransactionRepository transactionRepository;
	private final AccountRepository accountRepository;

	public TransactionListRespDto showDepositAndWithdraw(Long userId, Long accountNumber, String gubun, int page) {
		Account account = accountRepository.findByNumber(accountNumber)
			.orElseThrow(() -> new CustomApiException("해당 계좌를 찾을 수 없습니다."));

		account.checkOwner(userId);

		List<Transaction> transactionList = transactionRepository.findTransactionList(account.getId(), gubun, page);
		return new TransactionListRespDto(account, transactionList);
	}

}
