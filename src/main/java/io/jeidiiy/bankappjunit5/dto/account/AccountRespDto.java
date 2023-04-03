package io.jeidiiy.bankappjunit5.dto.account;

import java.util.List;
import java.util.stream.Collectors;

import io.jeidiiy.bankappjunit5.domain.account.Account;
import io.jeidiiy.bankappjunit5.domain.user.User;
import lombok.Getter;
import lombok.Setter;

public class AccountRespDto {
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
