package io.jeidiiy.bankappjunit5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BankAppJunit5Application {

	public static void main(String[] args) {
		SpringApplication.run(BankAppJunit5Application.class, args);
	}

}
