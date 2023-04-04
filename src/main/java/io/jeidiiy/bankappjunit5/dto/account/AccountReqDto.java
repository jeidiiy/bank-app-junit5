package io.jeidiiy.bankappjunit5.dto.account;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import io.jeidiiy.bankappjunit5.domain.account.Account;
import io.jeidiiy.bankappjunit5.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class AccountReqDto {
	@NoArgsConstructor
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
		@Pattern(regexp = "^(WITHDRAW)$")
		private String gubun;
	}

	@NoArgsConstructor
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

		public AccountDepositReqDto(Long number, Long amount, String gubun, String tel) {
			this.number = number;
			this.amount = amount;
			this.gubun = gubun;
			this.tel = tel;
		}
	}

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
