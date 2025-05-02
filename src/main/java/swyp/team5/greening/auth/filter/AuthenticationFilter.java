package swyp.team5.greening.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import swyp.team5.greening.auth.provider.TokenProvider;

@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        //1. 헤더에서 토큰 갖고 옴
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        //토큰 존재할 경우, 인증 필터 로직 수행
        if (StringUtils.hasText(token)) {
            //2. 토큰을 통해 userId 조회
            Long userId = tokenProvider.getPayLoad(token);

            //3. userId 컨트롤러에 넘김
            request.setAttribute("authorize-user-id", userId);
        }

        //4. 다음 필터로 흐름 넘김
        filterChain.doFilter(request, response);
    }
}
