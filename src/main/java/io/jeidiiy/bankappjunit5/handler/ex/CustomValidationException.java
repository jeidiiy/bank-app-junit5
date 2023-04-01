package io.jeidiiy.bankappjunit5.handler.ex;

import java.util.Map;

import lombok.Getter;

@Getter
public class CustomValidationException extends RuntimeException {
	private final Map<String, String> errorMap;

	public CustomValidationException(String msg, Map<String, String> errorMap) {
		super(msg);
		this.errorMap = errorMap;
	}
}
