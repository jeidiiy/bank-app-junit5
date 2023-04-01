package io.jeidiiy.bankappjunit5.web;

import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.jeidiiy.bankappjunit5.dto.ResponseDto;
import io.jeidiiy.bankappjunit5.dto.user.UserRequestDto;
import io.jeidiiy.bankappjunit5.dto.user.UserResponseDto;
import io.jeidiiy.bankappjunit5.service.UserService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class UserController {

	private final UserService userService;

	@PostMapping("/join")
	public ResponseEntity<ResponseDto<?>> join(
		@RequestBody @Validated UserRequestDto requestDto, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			HashMap<String, String> errorMap = new HashMap<>();

			for (FieldError error : bindingResult.getFieldErrors()) {
				errorMap.put(error.getField(), error.getDefaultMessage());
			}

			return new ResponseEntity<>(new ResponseDto<>(-1, "유효성 검사 실패", errorMap), HttpStatus.BAD_REQUEST);
		}
		UserResponseDto responseDto = userService.signup(requestDto);
		return new ResponseEntity<>(new ResponseDto<>(1, "회원가입 성공", responseDto), HttpStatus.CREATED);
	}
}
