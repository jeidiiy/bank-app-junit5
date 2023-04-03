package io.jeidiiy.bankappjunit5.config.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jeidiiy.bankappjunit5.config.auth.LoginUser;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

	public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws
		ServletException,
		IOException {
		if (isHeaderVerify(request)) {
			String token = request.getHeader(JwtVO.HEADER).replace(JwtVO.TOKEN_PREFIX, "");
			LoginUser loginUser = JwtProcess.verify(token);

			UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());

			SecurityContextHolder.getContext().setAuthentication(authenticationToken);

		}
		chain.doFilter(request, response);
	}

	private boolean isHeaderVerify(HttpServletRequest request) {
		String header = request.getHeader(JwtVO.HEADER);
		return header != null && header.startsWith(JwtVO.TOKEN_PREFIX);
	}
}
