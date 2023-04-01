package io.jeidiiy.bankappjunit5.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.jeidiiy.bankappjunit5.config.dummy.DummyObject;
import io.jeidiiy.bankappjunit5.domain.user.User;
import io.jeidiiy.bankappjunit5.domain.user.UserRepository;
import io.jeidiiy.bankappjunit5.dto.user.UserRequestDto;
import io.jeidiiy.bankappjunit5.dto.user.UserResponseDto;

@ExtendWith(MockitoExtension.class)
class UserServiceTests extends DummyObject {
	private final Logger log = LoggerFactory.getLogger(getClass());

	@InjectMocks
	private UserService userService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Test
	void 회원가입_test() {
		UserRequestDto joinReqDto = new UserRequestDto();
		joinReqDto.setUsername("test");
		joinReqDto.setPassword("1234");
		joinReqDto.setEmail("test@gmail.com");
		joinReqDto.setFullName("스트 테");

		// stub 1
		when(userRepository.findByUsername(any())).thenReturn(Optional.empty());

		// stub 2
		User mockUser = newMockUser(1L, "test", "스트 테");
		when(userRepository.save(any())).thenReturn(mockUser);

		// when
		UserResponseDto responseDto = userService.signup(joinReqDto);
		log.info("JoinResDto: {}", responseDto);

		// then
		assertThat(responseDto.getId()).isEqualTo(1L);
		assertThat(responseDto.getUsername()).isEqualTo(mockUser.getUsername());
	}

}
