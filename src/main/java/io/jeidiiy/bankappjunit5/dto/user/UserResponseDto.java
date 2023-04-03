package io.jeidiiy.bankappjunit5.dto.user;

import io.jeidiiy.bankappjunit5.domain.user.User;
import io.jeidiiy.bankappjunit5.util.CustomDateUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class UserResponseDto {
	private Long id;
	private String username;
	private String fullName;

	public UserResponseDto(User user) {
		this.id = user.getId();
		this.username = user.getUsername();
		this.fullName = user.getFullName();
	}

	@Getter
	@Setter
	@AllArgsConstructor
	public static class LoginRespDto {
		private Long id;
		private String username;
		private String createdAt;

		public static LoginRespDto of(User user) {
			return new LoginRespDto(user.getId(), user.getUsername(),
				CustomDateUtil.toStringFormat(user.getCreatedAt()));
		}
	}
}
