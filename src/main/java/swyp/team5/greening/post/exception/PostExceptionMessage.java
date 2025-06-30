package swyp.team5.greening.post.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import swyp.team5.greening.common.exception.ExceptionMessage;

@Getter
@RequiredArgsConstructor
public enum PostExceptionMessage implements ExceptionMessage {

    NOT_FOUND_POST("존재하지 않는 게시글입니다", "404"),
    BAD_REQUEST_POST_WRITER("게시글 작성자만 접근 가능합니다", "400");

    private final String message;
    private final String code;

}
