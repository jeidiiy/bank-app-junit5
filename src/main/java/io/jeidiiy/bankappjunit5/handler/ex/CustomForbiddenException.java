package io.jeidiiy.bankappjunit5.handler.ex;

public class CustomForbiddenException extends RuntimeException {
	public CustomForbiddenException(String message) {
		super(message);
	}
}
