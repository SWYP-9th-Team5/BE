package swyp.team5.greening.comment.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import swyp.team5.greening.common.exception.ExceptionMessage;

@Getter
@RequiredArgsConstructor
public enum PostExceptionMessage implements ExceptionMessage {

    NOT_FOUND_POST("존재하지 않는 게시글입니다", "404");

    private final String message;
    private final String code;

}
