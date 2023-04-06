package io.jeidiiy.bankappjunit5.config.dummy;

import java.time.LocalDateTime;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import io.jeidiiy.bankappjunit5.domain.account.Account;
import io.jeidiiy.bankappjunit5.domain.account.AccountRepository;
import io.jeidiiy.bankappjunit5.domain.transaction.Transaction;
import io.jeidiiy.bankappjunit5.domain.transaction.TransactionEnum;
import io.jeidiiy.bankappjunit5.domain.user.User;
import io.jeidiiy.bankappjunit5.domain.user.UserEnum;

public class DummyObject {

	protected Transaction newTransferTransaction(Account withdrawAccount, Account depositAccount,
		AccountRepository accountRepository) {
		withdrawAccount.withdraw(100L);
		depositAccount.deposit(100L);

		if (accountRepository != null) {
			accountRepository.save(withdrawAccount);
			accountRepository.save(depositAccount);
		}

		return Transaction.builder()
			.withdrawAccount(withdrawAccount)
			.depositAccount(depositAccount)
			.withdrawAccountBalance(withdrawAccount.getBalance())
			.depositAccountBalance(depositAccount.getBalance())
			.amount(100L)
			.gubun(TransactionEnum.TRANSFER)
			.sender(withdrawAccount.getNumber() + "")
			.receiver(depositAccount.getNumber() + "")
			.tel("01011112222")
			.build();
	}

	protected Transaction newWithdrawTransaction(Account account, AccountRepository accountRepository) {
		account.withdraw(100L);

		if (accountRepository != null) {
			accountRepository.save(account);
		}

		return Transaction.builder()
			.withdrawAccount(account)
			.depositAccount(null)
			.withdrawAccountBalance(account.getBalance())
			.depositAccountBalance(null)
			.amount(100L)
			.gubun(TransactionEnum.WITHDRAW)
			.sender("ATM")
			.receiver(account.getNumber() + "")
			.tel("01011112222")
			.build();
	}

	protected Transaction newDepositTransaction(Account account, AccountRepository accountRepository) {
		account.deposit(100L);

		if (accountRepository != null) {
			accountRepository.save(account);
		}

		return Transaction.builder()
			.withdrawAccount(null)
			.depositAccount(account)
			.withdrawAccountBalance(null)
			.depositAccountBalance(account.getBalance())
			.amount(100L)
			.gubun(TransactionEnum.DEPOSIT)
			.sender("ATM")
			.receiver(account.getNumber() + "")
			.tel("01011112222")
			.build();
	}

	protected Transaction newMockDepositTransaction(
		Long id, Account depositAccount) {
		depositAccount.deposit(100L);
		return Transaction.builder()
			.id(id)
			.depositAccount(depositAccount)
			.withdrawAccount(null)
			.depositAccountBalance(depositAccount.getBalance())
			.amount(100L)
			.gubun(TransactionEnum.DEPOSIT)
			.sender("ATM")
			.receiver(depositAccount.getNumber() + "")
			.tel("01011112222")
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.build();
	}

	protected Account newAccount(Long number, User user) {
		return Account.builder()
			.number(number)
			.password(1234L)
			.balance(1000L)
			.user(user)
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.build();
	}

	protected Account newMockAccount(Long id, Long number, Long balance, User user) {
		return Account.builder()
			.id(id)
			.number(number)
			.password(1234L)
			.balance(balance)
			.user(user)
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.build();
	}

	protected User newUser(String username, String fullName) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode("1234");

		return User.builder()
			.username(username)
			.password(encodedPassword)
			.email(username + "@gmail.com")
			.fullName(fullName)
			.role(UserEnum.CUSTOMER)
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.build();
	}

	protected User newMockUser(Long id, String username, String fullName) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode("1234");

		return User.builder()
			.id(id)
			.username(username)
			.password(encodedPassword)
			.email(username + "@gmail.com")
			.fullName(fullName)
			.role(UserEnum.CUSTOMER)
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.build();
	}
}
