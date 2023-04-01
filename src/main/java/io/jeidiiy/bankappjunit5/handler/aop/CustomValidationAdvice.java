package io.jeidiiy.bankappjunit5.handler.aop;

import java.util.HashMap;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import io.jeidiiy.bankappjunit5.handler.ex.CustomValidationException;

@Component
@Aspect
public class CustomValidationAdvice {

	@Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
	public void postMapping() {
	}

	@Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
	public void putMapping() {
	}

	@Around("postMapping() || putMapping()")
	public Object validationAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		Object[] args = proceedingJoinPoint.getArgs();
		for (Object arg : args) {
			if (arg instanceof BindingResult) {
				BindingResult bindingResult = (BindingResult)arg;

				if (bindingResult.hasErrors()) {
					HashMap<String, String> errorMap = new HashMap<>();

					for (FieldError error : bindingResult.getFieldErrors()) {
						errorMap.put(error.getField(), error.getDefaultMessage());
					}
					throw new CustomValidationException("유효성검사 실패", errorMap);
				}
			}
		}

		return proceedingJoinPoint.proceed();
	}
}
