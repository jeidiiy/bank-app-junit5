package io.jeidiiy.bankappjunit5.domain.account;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
	Optional<Account> findByNumber(Long number);

	// select * from account where user_id = :id
	List<Account> findByUser_id(Long id);
}
