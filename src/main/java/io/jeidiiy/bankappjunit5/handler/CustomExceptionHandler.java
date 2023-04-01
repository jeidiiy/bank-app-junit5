package io.jeidiiy.bankappjunit5.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jeidiiy.bankappjunit5.dto.ResponseDto;
import io.jeidiiy.bankappjunit5.handler.ex.CustomApiException;

@RestControllerAdvice
public class CustomExceptionHandler {
	private final Logger log = LoggerFactory.getLogger(getClass());

	@ExceptionHandler(CustomApiException.class)
	public ResponseEntity<?> apiException(CustomApiException ex) {
		log.error(ex.getMessage());
		return new ResponseEntity<>(new ResponseDto<>(-1, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
	}
}
