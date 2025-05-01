package swyp.team5.greening.auth.config;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import swyp.team5.greening.auth.filter.AuthenticationFilter;
import swyp.team5.greening.auth.filter.JwtExceptionHandlerFilter;
import swyp.team5.greening.auth.infrastructure.JwtTokenProvider;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    private final JwtTokenProvider jwtTokenProvider;

    private JwtExceptionHandlerFilter jwtExceptionHandlerFilter;
    private AuthenticationFilter authenticationFilter;

    //필터 체인 -> 예외 관련 예외 필터 추가
    @Bean
    public FilterRegistrationBean<Filter> exceptionHandlerFilter() {
        jwtExceptionHandlerFilter = new JwtExceptionHandlerFilter();

        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(jwtExceptionHandlerFilter);

        registrationBean.addUrlPatterns("/api/*"); // 적용할 URL 패턴
        registrationBean.setOrder(1); // 필터 순서 (낮을수록 먼저 실행됨)

        return registrationBean;
    }

    //필터 체인 -> 인증 필터 추가
    @Bean
    public FilterRegistrationBean<Filter> authenticationFilter() {
        authenticationFilter = new AuthenticationFilter(jwtTokenProvider);

        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(authenticationFilter);

        registrationBean.addUrlPatterns("/api/*");
        registrationBean.setOrder(2);

        return registrationBean;
    }
}
