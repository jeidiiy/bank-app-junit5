package io.jeidiiy.bankappjunit5.domain.user;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "user_tb")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true, nullable = false, length = 20)
	private String username;

	@Column(nullable = false, length = 60)
	private String password;

	@Column(nullable = false, length = 20)
	private String email;

	@Column(nullable = false, length = 20)
	private String fullName;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private UserEnum role;

	@CreatedDate
	@Column(nullable = false)
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(nullable = false)
	private LocalDateTime updatedAt;

	@Builder
	public User(Long id, String username, String password, String email, String fullName, UserEnum role,
		LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.fullName = fullName;
		this.role = role;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
}
