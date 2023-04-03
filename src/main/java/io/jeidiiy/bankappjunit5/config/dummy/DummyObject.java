package io.jeidiiy.bankappjunit5.config.dummy;

import java.time.LocalDateTime;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import io.jeidiiy.bankappjunit5.domain.account.Account;
import io.jeidiiy.bankappjunit5.domain.user.User;
import io.jeidiiy.bankappjunit5.domain.user.UserEnum;

public class DummyObject {

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
