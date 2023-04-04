package io.jeidiiy.bankappjunit5.domain.account;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import io.jeidiiy.bankappjunit5.domain.user.User;
import io.jeidiiy.bankappjunit5.handler.ex.CustomApiException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "account_tb", indexes = {
	@Index(name = "idx_account_number", columnList = "number")
})
public class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true, nullable = false, length = 4)
	private Long number;
	@Column(nullable = false, length = 4)
	private Long password;
	@Column(nullable = false)
	private Long balance;

	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	@CreatedDate
	@Column(nullable = false)
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(nullable = false)
	private LocalDateTime updatedAt;

	@Builder
	public Account(Long id, Long number, Long password, Long balance, User user, LocalDateTime createdAt,
		LocalDateTime updatedAt) {
		this.id = id;
		this.number = number;
		this.password = password;
		this.balance = balance;
		this.user = user;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	// Lazy 로딩이어도 id를 조회할 때는 이미 값이 들어있으므로 select 쿼리가 전송되지 않는다.
	public void checkOwner(Long userId) {
		// user.getUsername();
		if (user.getId().compareTo(userId) != 0) {
			throw new CustomApiException("계좌 소유자가 아닙니다");
		}
	}

	public void deposit(Long amount) {
		balance += amount;
	}

	public void checkSamePassword(Long password) {
		if (!Objects.equals(this.password, password)) {
			throw new CustomApiException("계좌 비밀번호 검증에 실패했습니다");
		}
	}

	public void checkBalance(Long amount) {
		if (this.balance < amount) {
			throw new CustomApiException("계좌 잔액이 부족합니다");
		}
	}

	public void withdraw(Long amount) {
		checkBalance(amount);
		balance -= amount;
	}
}
