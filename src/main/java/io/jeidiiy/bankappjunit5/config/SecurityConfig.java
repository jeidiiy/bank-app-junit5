package io.jeidiiy.bankappjunit5.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import io.jeidiiy.bankappjunit5.config.jwt.JwtAuthenticationFilter;
import io.jeidiiy.bankappjunit5.domain.user.UserEnum;
import io.jeidiiy.bankappjunit5.util.CustomResponseUtil;

@Configuration
public class SecurityConfig {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.headers().frameOptions().disable(); // iframe 허용안함
		http.csrf().disable(); // enable하면 postman 동작 안 함
		http.cors().configurationSource(corsConfigurationSource());

		// jSessionId를 서버에서 관리 안 하겠다는 의미
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		// react, 앱으로 요청할 예정
		http.formLogin().disable();
		// httpBasic은 브라우저가 팝업 창을 이용해서 사용자 인증을 진행한다.
		http.httpBasic().disable();

		http.apply(new CustomSecurityFilterManager());

		// Exception 가로채기
		http.exceptionHandling().authenticationEntryPoint((req, res, authException) ->
			CustomResponseUtil.unAuthentication(res, "로그인을 진행해 주세요"));

		http.authorizeRequests().antMatchers("/api/s/**").authenticated()
			.antMatchers("/api/admin/**").hasRole("" + UserEnum.ADMIN)
			.anyRequest().permitAll();

		return http.build();
	}

	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.addAllowedHeader("*");
		corsConfiguration.addAllowedMethod("*");
		corsConfiguration.addAllowedOriginPattern("*");
		corsConfiguration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfiguration);

		return source;
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public class CustomSecurityFilterManager extends AbstractHttpConfigurer<CustomSecurityFilterManager, HttpSecurity> {
		@Override
		public void configure(HttpSecurity builder) throws Exception {
			AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
			builder.addFilter(new JwtAuthenticationFilter(authenticationManager));
			super.configure(builder);
		}
	}
}
