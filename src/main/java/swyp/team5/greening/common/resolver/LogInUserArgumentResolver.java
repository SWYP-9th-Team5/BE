package swyp.team5.greening.common.resolver;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import swyp.team5.greening.auth.exception.AuthException;
import swyp.team5.greening.auth.exception.AuthExceptionMessage;
import swyp.team5.greening.common.exception.GreeningGlobalException;
import swyp.team5.greening.user.domain.repository.UserRepository;
import swyp.team5.greening.user.exception.UserExceptionMessage;

@Component
@RequiredArgsConstructor
public class LogInUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final UserRepository userRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(LogIn.class);
        boolean hasUserType = Long.class.isAssignableFrom(parameter.getParameterType());

        return hasLoginAnnotation && hasUserType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        //필터에서 parsing 한 userId 조회
        Long userId = (Long) request.getAttribute("authorize-user-id");

        //만약 user 정보가 요청 정보에 담겨있지 않다면, 인증 예외
        if (Objects.isNull(userId)) {
            throw new AuthException(AuthExceptionMessage.UNAUTHORIZED);
        }

        //존재하지 않는 유저라면
        if (!userRepository.existsById(userId)) {
            throw new GreeningGlobalException(UserExceptionMessage.NOT_FOUND_USER);
        }

        return userId;
    }
}