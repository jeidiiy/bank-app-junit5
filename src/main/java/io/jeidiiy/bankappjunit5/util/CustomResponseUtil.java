package io.jeidiiy.bankappjunit5.util;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jeidiiy.bankappjunit5.dto.ResponseDto;

public class CustomResponseUtil {

	private static final Logger log = LoggerFactory.getLogger(CustomResponseUtil.class);

	public static void success(HttpServletResponse res, Object dto) {
		try {
			ObjectMapper om = new ObjectMapper();
			ResponseDto<?> responseDto = new ResponseDto<>(1, "로그인성공", dto);
			String responseBody = om.writeValueAsString(responseDto);
			res.setContentType("application/json; charset=utf-8");
			res.setStatus(200);
			res.getWriter().println(responseBody);
		} catch (Exception exception) {
			log.error("서버 파싱 에러");
		}
	}

	public static void unAuthentication(HttpServletResponse res, String msg) {
		try {
			ObjectMapper om = new ObjectMapper();
			ResponseDto<?> responseDto = new ResponseDto<>(-1, "인증안됨", msg);
			String responseBody = om.writeValueAsString(responseDto);
			res.setContentType("application/json; charset=utf-8");
			res.setStatus(401);
			res.getWriter().println(responseBody);
		} catch (Exception exception) {
			log.error("서버 파싱 에러");
		}
	}
}
