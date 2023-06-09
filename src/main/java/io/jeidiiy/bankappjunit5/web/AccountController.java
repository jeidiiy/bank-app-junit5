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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.jeidiiy.bankappjunit5.config.auth.LoginUser;
import io.jeidiiy.bankappjunit5.dto.ResponseDto;
import io.jeidiiy.bankappjunit5.dto.account.AccountReqDto;
import io.jeidiiy.bankappjunit5.dto.account.AccountReqDto.AccountDepositReqDto;
import io.jeidiiy.bankappjunit5.dto.account.AccountReqDto.AccountTransferReqDto;
import io.jeidiiy.bankappjunit5.dto.account.AccountReqDto.AccountWithdrawReqDto;
import io.jeidiiy.bankappjunit5.service.AccountService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class AccountController {
	private final AccountService accountService;

	@GetMapping("/s/account/{number}")
	public ResponseEntity<?> findDetailAccount(
		@PathVariable Long number,
		@RequestParam(value = "page", defaultValue = "0") Integer page,
		@AuthenticationPrincipal LoginUser loginUser) {
		AccountDetailRespDto accountDetailRespDto = accountService.showAccountDetail(number,
			loginUser.getUser().getId(), page);
		return new ResponseEntity<>(new ResponseDto<>(1, "계좌 상세보기 성공", accountDetailRespDto), HttpStatus.OK);
	}

	@PostMapping("/s/account/transfer")
	public ResponseEntity<?> transferAccount(
		@RequestBody @Validated AccountTransferReqDto accountTransferReqDto, BindingResult bindingResult,
		@AuthenticationPrincipal LoginUser loginUser) {
		AccountTransferRespDto accountTransferRespDto = accountService.transfer(accountTransferReqDto,
			loginUser.getUser().getId());
		return new ResponseEntity<>(new ResponseDto<>(1, "계좌 이체 완료", accountTransferRespDto), HttpStatus.CREATED);
	}

	@PostMapping("/account/withdraw")
	public ResponseEntity<?> withdrawAccount(
		@RequestBody @Validated AccountWithdrawReqDto accountWithdrawReqDto,
		BindingResult bindingResult,
		@AuthenticationPrincipal LoginUser loginUser) {
		AccountWithdrawRespDto accountWithdrawRespDto =
			accountService.withdraw(accountWithdrawReqDto, loginUser.getUser().getId());
		return new ResponseEntity<>(new ResponseDto<>(1, "계좌 입금 완료", accountWithdrawRespDto), HttpStatus.CREATED);
	}

	@PostMapping("/account/deposit")
	public ResponseEntity<?> depositAccount(
		@RequestBody @Validated AccountDepositReqDto accountDepositReqDto,
		BindingResult bindingResult) {
		AccountDepositRespDto accountDepositRespDto = accountService.deposit(accountDepositReqDto);
		return new ResponseEntity<>(new ResponseDto<>(1, "계좌 입금 완료", accountDepositRespDto), HttpStatus.CREATED);
	}

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
