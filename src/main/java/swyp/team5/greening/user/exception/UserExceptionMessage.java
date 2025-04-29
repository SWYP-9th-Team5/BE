package swyp.team5.greening.user.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import swyp.team5.greening.common.exception.ExceptionMessage;

@Getter
@RequiredArgsConstructor
public enum UserExceptionMessage implements ExceptionMessage {

    NOT_FOUND_USER("존재하지 않는 회원입니다", "404");

    private final String message;
    private final String code;
}
