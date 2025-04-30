package swyp.team5.greening.auth.exception;

import lombok.Getter;
import swyp.team5.greening.common.exception.ExceptionMessage;

//인증 인가에서 사용되는 예외 클래스
@Getter
public class AuthException extends RuntimeException{

    private final String code;

    public AuthException(ExceptionMessage exceptionMessage) {
        super(exceptionMessage.getMessage());
        this.code = exceptionMessage.getCode();
    }
}
