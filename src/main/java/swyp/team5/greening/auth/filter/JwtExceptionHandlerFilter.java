package swyp.team5.greening.auth.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import swyp.team5.greening.auth.exception.AuthException;
import swyp.team5.greening.common.exception.ExceptionResponse;

@Component
@Slf4j
public class JwtExceptionHandlerFilter extends OncePerRequestFilter {

    private final ObjectMapper mapper = new ObjectMapper();

    private String toJson(ExceptionResponse response) throws JsonProcessingException {
        return mapper.writeValueAsString(response);
    }

    private void sendJson(HttpServletResponse response, String resultJson) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");
        response.setContentLength(resultJson.getBytes().length);
        response.getWriter().write(resultJson);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch(AuthException ve) {
            log.info("{}", ve.getMessage());

            //예외 발생 시, JSON 응답으로 예외 메시지 전달
            sendJson(response, toJson(new ExceptionResponse(ve.getMessage())));
        }
    }
}
