package io.jeidiiy.bankappjunit5.dto.account;

import io.jeidiiy.bankappjunit5.domain.account.Account;
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
}
