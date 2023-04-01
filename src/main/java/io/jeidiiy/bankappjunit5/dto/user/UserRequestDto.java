package io.jeidiiy.bankappjunit5.dto.user;

import javax.validation.constraints.NotEmpty;

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
	@NotEmpty
	private String username;
	@NotEmpty
	private String password;
	@NotEmpty
	private String email;
	@NotEmpty
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
