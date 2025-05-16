package swyp.team5.greening.mbti.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import swyp.team5.greening.common.exception.ExceptionMessage;

@Getter
@RequiredArgsConstructor
public enum MbtiExceptionMessage implements ExceptionMessage {

    NOT_FOUND_MBTI_TYPE("올바르지 않은 MBTI 타입입니다.", "400"),
    NOT_FOUND_MBTI("존재하지 않는 MBTI 입니다.", "404");

    private final String message;
    private final String code;
}
