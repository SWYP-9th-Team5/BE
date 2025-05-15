package swyp.team5.greening.common.resolver;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class OptionalLogInArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(OptionalLogIn.class);
        boolean hasUserType = Long.class.isAssignableFrom(parameter.getParameterType());

        return hasLoginAnnotation && hasUserType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        //필터에서 parsing 한 userId 조회
        Long userId = (Long) request.getAttribute("authorize-user-id");

        //유저가 로그인을 했든 안했든, userId를 넘김
        return userId;
    }
}
