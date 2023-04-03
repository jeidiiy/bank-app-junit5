package io.jeidiiy.bankappjunit5.web;

import static io.jeidiiy.bankappjunit5.dto.account.AccountReqDto.*;
import static io.jeidiiy.bankappjunit5.dto.account.AccountRespDto.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.jeidiiy.bankappjunit5.config.auth.LoginUser;
import io.jeidiiy.bankappjunit5.dto.ResponseDto;
import io.jeidiiy.bankappjunit5.service.AccountService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class AccountController {
	private final AccountService accountService;

	@PostMapping("/s/account")
	public ResponseEntity<?> saveAccount(
		@RequestBody @Validated AccountSaveReqDto accountSaveReqDto,
		BindingResult bindingResult,
		@AuthenticationPrincipal LoginUser loginUser) {
		AccountSaveRespDto accountSaveRespDto = accountService.register(accountSaveReqDto,
			loginUser.getUser().getId());
		return new ResponseEntity<>(new ResponseDto<>(1, "게좌등록 성공", accountSaveRespDto), HttpStatus.CREATED);
	}
}
