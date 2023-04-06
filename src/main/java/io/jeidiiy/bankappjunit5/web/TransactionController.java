package io.jeidiiy.bankappjunit5.web;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.jeidiiy.bankappjunit5.config.auth.LoginUser;
import io.jeidiiy.bankappjunit5.dto.ResponseDto;
import io.jeidiiy.bankappjunit5.dto.transaction.TransactionListRespDto;
import io.jeidiiy.bankappjunit5.service.TransactionService;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class TransactionController {

	private final TransactionService transactionService;

	@GetMapping("/s/account/{number}/transaction")
	public ResponseEntity<?> findTransactionList(@PathVariable Long number,
		@RequestParam(value = "gubun", defaultValue = "ALL") String gubun,
		@RequestParam(value = "page", defaultValue = "0") Integer page,
		@AuthenticationPrincipal LoginUser loginUser) {
		TransactionListRespDto transactionListRespDto = transactionService.showDepositAndWithdraw(
			loginUser.getUser().getId(), number, gubun, page);
		return ResponseEntity.ok(new ResponseDto<>(1, "입출금목록 보기 성공", transactionListRespDto));
	}
}
