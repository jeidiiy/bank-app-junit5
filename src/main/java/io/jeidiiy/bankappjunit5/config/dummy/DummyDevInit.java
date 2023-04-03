package io.jeidiiy.bankappjunit5.config.dummy;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.jeidiiy.bankappjunit5.domain.user.User;
import io.jeidiiy.bankappjunit5.domain.user.UserRepository;

@Configuration
public class DummyDevInit extends DummyObject {

	@Profile("dev")
	@Bean
	public CommandLineRunner init(UserRepository userRepository) {
		return (args) -> {
			User user = userRepository.save(newUser("test", "스트테"));
		};
	}
}
