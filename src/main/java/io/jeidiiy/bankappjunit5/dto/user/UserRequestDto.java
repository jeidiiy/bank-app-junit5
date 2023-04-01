package io.jeidiiy.bankappjunit5.dto.user;

import org.springframework.security.crypto.password.PasswordEncoder;

import io.jeidiiy.bankappjunit5.domain.user.User;
import io.jeidiiy.bankappjunit5.domain.user.UserEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class UserRequestDto {
	// 유효성 검사
	private String username;
	private String password;
	private String email;
	private String fullName;

	public User toEntity(PasswordEncoder passwordEncoder) {
		return User.builder()
			.username(username)
			.password(passwordEncoder.encode(password))
			.email(email)
			.fullName(fullName)
			.role(UserEnum.CUSTOMER)
			.build();
	}
}
