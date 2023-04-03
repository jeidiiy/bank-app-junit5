package io.jeidiiy.bankappjunit5.config.jwt;

import static io.jeidiiy.bankappjunit5.dto.user.UserRequestDto.*;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jeidiiy.bankappjunit5.config.auth.LoginUser;
import io.jeidiiy.bankappjunit5.dto.user.UserResponseDto.LoginRespDto;
import io.jeidiiy.bankappjunit5.util.CustomResponseUtil;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;

	public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
		setFilterProcessesUrl("/api/login");
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
		AuthenticationException {
		try {
			ObjectMapper om = new ObjectMapper();
			LoginReqDto loginReqDto = om.readValue(request.getInputStream(), LoginReqDto.class);

			// 강제 로그인
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				loginReqDto.getUsername(), loginReqDto.getPassword());

			return authenticationManager.authenticate(authenticationToken);
		} catch (Exception exception) {
			throw new InternalAuthenticationServiceException(exception.getMessage());
		}
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException failed) {
		CustomResponseUtil.fail(response, "로그인실패", HttpStatus.UNAUTHORIZED);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
		Authentication authResult) {
		LoginUser loginUser = (LoginUser)authResult.getPrincipal();
		String jwtToken = JwtProcess.create(loginUser);
		response.addHeader(JwtVO.HEADER, jwtToken);

		LoginRespDto loginRespDto = LoginRespDto.of(loginUser.getUser());
		CustomResponseUtil.success(response, loginRespDto);
	}
}
