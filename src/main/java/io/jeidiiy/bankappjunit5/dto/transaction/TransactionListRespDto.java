package io.jeidiiy.bankappjunit5.dto.transaction;

import java.util.List;
import java.util.stream.Collectors;

import io.jeidiiy.bankappjunit5.domain.account.Account;
import io.jeidiiy.bankappjunit5.domain.transaction.Transaction;
import io.jeidiiy.bankappjunit5.util.CustomDateUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionListRespDto {
	private List<TransactionDto> transactionDtos;

	public TransactionListRespDto(Account account, List<Transaction> transactions) {
		this.transactionDtos = transactions.stream()
			.map(transaction -> new TransactionDto(transaction, account.getNumber()))
			.collect(Collectors.toList());
	}

	@Getter
	@Setter
	public static class TransactionDto {
		private Long id;
		private String gubun;
		private Long amount;
		private String sender;
		private String receiver;
		private String tel;
		private String createdAt;
		private Long balance;

		public TransactionDto(Transaction transaction, Long accountNumber) {
			this.id = transaction.getId();
			this.gubun = transaction.getGubun().getValue();
			this.amount = transaction.getAmount();
			this.sender = transaction.getSender();
			this.receiver = transaction.getReceiver();
			this.createdAt = CustomDateUtil.toStringFormat(transaction.getCreatedAt());
			this.tel = transaction.getTel() == null ? "없음" : transaction.getTel();

			if (transaction.getDepositAccount() == null) {
				this.balance = transaction.getWithdrawAccountBalance();
			} else if (transaction.getWithdrawAccount() == null) {
				this.balance = transaction.getDepositAccountBalance();
			} else {
				if (accountNumber.longValue() == transaction.getDepositAccount().getNumber()) {
					this.balance = transaction.getDepositAccountBalance();
				} else {
					this.balance = transaction.getWithdrawAccountBalance();
				}
			}
		}
	}
}
