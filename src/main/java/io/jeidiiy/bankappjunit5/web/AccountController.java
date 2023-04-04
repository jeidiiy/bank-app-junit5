package io.jeidiiy.bankappjunit5.web;

import static io.jeidiiy.bankappjunit5.dto.account.AccountRespDto.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.jeidiiy.bankappjunit5.config.auth.LoginUser;
import io.jeidiiy.bankappjunit5.dto.ResponseDto;
import io.jeidiiy.bankappjunit5.dto.account.AccountReqDto;
import io.jeidiiy.bankappjunit5.service.AccountService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class AccountController {
	private final AccountService accountService;

	@DeleteMapping("/s/account/{number}")
	public ResponseEntity<?> deleteAccount(@PathVariable Long number, @AuthenticationPrincipal LoginUser loginUser) {
		accountService.delete(number, loginUser.getUser().getId());
		return new ResponseEntity<>(new ResponseDto<>(1, "계좌 삭제 완료", null), HttpStatus.OK);
	}

	@PostMapping("/s/account")
	public ResponseEntity<?> saveAccount(
		@RequestBody @Validated AccountReqDto.AccountSaveReqDto accountSaveReqDto,
		BindingResult bindingResult,
		@AuthenticationPrincipal LoginUser loginUser) {
		AccountSaveRespDto accountSaveRespDto = accountService.register(accountSaveReqDto,
			loginUser.getUser().getId());
		return new ResponseEntity<>(new ResponseDto<>(1, "게좌등록 성공", accountSaveRespDto), HttpStatus.CREATED);
	}

	@GetMapping("/s/account/login-user")
	public ResponseEntity<?> findLoggedInUserAccount(@AuthenticationPrincipal LoginUser loginUser) {
		AccountListRespDto accountListRespDto = accountService.findByUserId(loginUser.getUser().getId());
		return new ResponseEntity<>(new ResponseDto<>(1, "유저별 계좌목록확인 성공", accountListRespDto), HttpStatus.OK);
	}
}
