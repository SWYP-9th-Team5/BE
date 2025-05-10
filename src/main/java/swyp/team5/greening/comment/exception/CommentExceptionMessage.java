package swyp.team5.greening.comment.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import swyp.team5.greening.common.exception.ExceptionMessage;

@Getter
@RequiredArgsConstructor
public enum CommentExceptionMessage implements ExceptionMessage {

    NOT_FOUND_COMMENT("존재하지 않는 댓글입니다", "404"),
    BAD_REQUEST_COMMENT_WRITER("댓글 작성자만 접근 가능합니다", "400");

    private final String message;
    private final String code;
}
