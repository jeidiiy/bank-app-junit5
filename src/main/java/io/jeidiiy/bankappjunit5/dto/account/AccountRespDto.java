package io.jeidiiy.bankappjunit5.dto.account;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.jeidiiy.bankappjunit5.domain.account.Account;
import io.jeidiiy.bankappjunit5.domain.transaction.Transaction;
import io.jeidiiy.bankappjunit5.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class AccountRespDto {

	@Getter
	@Setter
	public static class AccountDepositRespDto {
		private Long id;
		private Long number;
		private TransactionDto transactionDto;

		public AccountDepositRespDto(Account account, Transaction transaction) {
			this.id = account.getId();
			this.number = account.getNumber();
			this.transactionDto = new TransactionDto(transaction);
		}

		@Getter
		@Setter
		public static class TransactionDto {
			private Long id;
			private String gubun;
			private String sender;
			private String receiver;
			private Long amount;
			@JsonIgnore
			private Long depositAccountBalance; // 클라이언트에 전달 X -> 서비스에서 테스트 용도
			private String tel;
			private String createdAt;

			public TransactionDto(Transaction transaction) {
				this.id = transaction.getId();
				this.gubun = transaction.getGubun().getValue();
				this.sender = transaction.getSender();
				this.receiver = transaction.getReceiver();
				this.amount = transaction.getAmount();
				this.depositAccountBalance = transaction.getDepositAccountBalance();
				this.tel = transaction.getTel();
				this.createdAt = transaction.getCreatedAt().toString();
			}
		}
	}

	@Getter
	@Setter
	public static class AccountDepositReqDto {
		@NotNull
		@Digits(integer = 4, fraction = 4)
		private Long number;
		@NotNull
		private Long amount;
		@NotEmpty
		@Pattern(regexp = "^(DEPOSIT)$")
		private String gubun;
		@NotEmpty
		@Pattern(regexp = "^[0-9]{11}")
		private String tel;

		@Builder
		public AccountDepositReqDto(Long number, Long amount, String gubun, String tel) {
			this.number = number;
			this.amount = amount;
			this.gubun = gubun;
			this.tel = tel;
		}
	}

	@Getter
	@Setter
	public static class AccountSaveRespDto {
		private Long id;
		private Long number;
		private Long balance;

		public AccountSaveRespDto(Account account) {
			this.id = account.getId();
			this.number = account.getNumber();
			this.balance = account.getBalance();
		}
	}

	@Getter
	@Setter
	public static class AccountListRespDto {
		private String fullName;
		private List<AccountDto> accounts;

		public AccountListRespDto(User user, List<Account> accounts) {
			this.fullName = user.getFullName();
			this.accounts = accounts.stream().map(AccountDto::new).collect(Collectors.toList());
		}

		@Getter
		@Setter
		public static class AccountDto {
			private Long id;
			private Long number;
			private Long balance;

			public AccountDto(Account account) {
				this.id = account.getId();
				this.number = account.getNumber();
				this.balance = account.getBalance();
			}
		}
	}
}
