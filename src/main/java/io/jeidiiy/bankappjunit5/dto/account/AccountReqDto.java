package io.jeidiiy.bankappjunit5.dto.account;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import io.jeidiiy.bankappjunit5.domain.account.Account;
import io.jeidiiy.bankappjunit5.domain.user.User;
import lombok.Getter;
import lombok.Setter;

public class AccountReqDto {
	@Getter
	@Setter
	public static class AccountSaveReqDto {
		@NotNull
		@Digits(integer = 4, fraction = 4)
		private Long number;

		@NotNull
		@Digits(integer = 4, fraction = 4)
		private Long password;

		public Account toEntity(User user) {
			return Account.builder()
				.number(number)
				.password(password)
				.balance(1000L)
				.user(user)
				.build();
		}
	}
}
