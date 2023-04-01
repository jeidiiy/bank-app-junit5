package io.jeidiiy.bankappjunit5.handler.ex;

public class CustomApiException extends RuntimeException {
	public CustomApiException(String message) {
		super(message);
	}
}
