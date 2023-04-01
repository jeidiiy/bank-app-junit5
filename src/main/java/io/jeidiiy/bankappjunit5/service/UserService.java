package io.jeidiiy.bankappjunit5.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.jeidiiy.bankappjunit5.domain.user.User;
import io.jeidiiy.bankappjunit5.domain.user.UserRepository;
import io.jeidiiy.bankappjunit5.dto.user.UserRequestDto;
import io.jeidiiy.bankappjunit5.dto.user.UserResponseDto;
import io.jeidiiy.bankappjunit5.handler.ex.CustomApiException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

	private final Logger log = LoggerFactory.getLogger(getClass());
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserResponseDto signup(final UserRequestDto requestDto) {
		// 1. 동일 유저네임 존재 검사
		Optional<User> userOp = userRepository.findByUsername(requestDto.getUsername());
		if (userOp.isPresent()) {
			throw new CustomApiException("동일한 username이 존재합니다.");
		}

		// 2. 패스워드 인코딩
		User userPS = userRepository.save(requestDto.toEntity(passwordEncoder));

		// 3. dto 응답
		return new UserResponseDto(userPS);
	}
}
