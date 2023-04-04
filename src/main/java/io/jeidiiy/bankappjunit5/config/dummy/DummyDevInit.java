package io.jeidiiy.bankappjunit5.config.dummy;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.jeidiiy.bankappjunit5.domain.account.Account;
import io.jeidiiy.bankappjunit5.domain.account.AccountRepository;
import io.jeidiiy.bankappjunit5.domain.user.User;
import io.jeidiiy.bankappjunit5.domain.user.UserRepository;

@Configuration
public class DummyDevInit extends DummyObject {

	@Profile("dev")
	@Bean
	public CommandLineRunner init(UserRepository userRepository, AccountRepository accountRepository) {
		return (args) -> {
			User test = userRepository.save(newUser("test", "스트테"));
			User toast = userRepository.save(newUser("toast", "스트토"));
			Account testAccount = accountRepository.save(newAccount(1111L, test));
			Account toastAccount = accountRepository.save(newAccount(2222L, toast));
		};
	}
}
