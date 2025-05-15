package swyp.team5.greening.postCategory.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import swyp.team5.greening.common.exception.ExceptionMessage;

@Getter
@RequiredArgsConstructor
public enum CategoryException implements ExceptionMessage {

    NOT_FOUND_CATEGORY("존재하지 않는 카테고리입니다.", "404");

    private final String message;
    private final String code;
}
