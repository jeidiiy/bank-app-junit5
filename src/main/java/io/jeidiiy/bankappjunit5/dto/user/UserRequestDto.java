package io.jeidiiy.bankappjunit5.dto.user;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.springframework.security.crypto.password.PasswordEncoder;

import io.jeidiiy.bankappjunit5.domain.user.User;
import io.jeidiiy.bankappjunit5.domain.user.UserEnum;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class UserRequestDto {
	// 영문, 숫자, 가능, 길이 2~20
	@Pattern(regexp = "^[a-zA-Z\\d]{2,20}$", message = "영문/숫자를 2~20자 이내로 작성해 주세요")
	@NotEmpty
	private String username;
	@NotEmpty
	private String password;
	@Pattern(regexp = "^[a-zA-Z\\d]{2,10}@[a-zA-Z\\d]{2,6}\\.[a-zA-Z]{2,3}$", message = "이메일 형식으로 작성해 주세요")
	@NotEmpty
	private String email;
	@Pattern(regexp = "^[a-zA-Z가-힣]{1,20}$", message = "한글/영문을 1~20자 이내로 작성해 주세요")
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

	@Data
	public static class LoginReqDto {
		private String username;
		private String password;
	}
}
