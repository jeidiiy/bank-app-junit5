package io.jeidiiy.bankappjunit5.temp;

import static org.assertj.core.api.Assertions.*;

import java.util.regex.Pattern;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RegexTest {

	@DisplayName("한글만 가능")
	@Test
	void onlyHangul() {
		String value = "가나다라";
		boolean result = Pattern.matches("^[가-힣]+$", value);
		assertThat(result).isTrue();
	}

	@DisplayName("한글 불가능")
	@Test
	void neverHangul() {
		String value = "abc";
		boolean result = Pattern.matches("^[^ㄱ-ㅎ가-힣]*$", value);
		assertThat(result).isTrue();
	}

	@DisplayName("영어만 가능")
	@Test
	void onlyEng() {
		String value = "abc";
		boolean result = Pattern.matches("^[a-zA-Z]+$", value);
		assertThat(result).isTrue();
	}

	@DisplayName("영어 불가능")
	@Test
	void neverEng() {
		String value = "한글";
		boolean result = Pattern.matches("^[^a-zA-Z]*$", value);
		assertThat(result).isTrue();
	}

	@DisplayName("영어와 숫자만 가능")
	@Test
	void onlyEngAndDigit() {
		String value = "a1b2c3";
		boolean result = Pattern.matches("^[a-zA-Z\\d]+$", value);
		assertThat(result).isTrue();
	}

	@DisplayName("2~4 길이의 영여만 가능")
	@Test
	void onlyEngAndMin2AndMax4() {
		String value = "abc";
		boolean result = Pattern.matches("^[a-zA-Z]{2,4}$", value);
		assertThat(result).isTrue();
	}

	@DisplayName("유저네임 정규식 테스트")
	@Test
	void username_test() {
		//given
		String username = "test";

		//when
		boolean result = Pattern.matches("^[a-zA-Z\\d]{2,20}$", username);

		//then
		assertThat(result).isTrue();
	}

	@DisplayName("풀네임 정규식 테스트")
	@Test
	void fullName_test() {
		//given
		String fullName = "test지용";

		//when
		boolean result = Pattern.matches("^[a-zA-Z가-힣]{1,20}$", fullName);

		//then
		assertThat(result).isTrue();
	}

	@DisplayName("이메일 정규식 테스트")
	@Test
	void email_test() {
		//given
		String validEmail = "test@gmail.com";

		//when
		boolean result = Pattern.matches("^[a-zA-Z\\d]{2,6}@[a-zA-Z\\d]{2,6}\\.[a-zA-Z]{2,3}$", validEmail);

		//then
		assertThat(result).isTrue();
	}
}
