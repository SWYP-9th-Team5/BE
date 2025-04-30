package swyp.team5.greening.auth.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import swyp.team5.greening.common.exception.ExceptionMessage;

@Getter
@RequiredArgsConstructor
public enum AuthExceptionMessage implements ExceptionMessage {

    AUTH_TOKEN_MALFORMED("유효하지 않은 토큰입니다", "401"),
    AUTH_TOKEN_EXPIRED("기한이 만료된 토큰입니다", "401"),
    AUTH_TOKEN_UNSUPPORTED("지원하지 않는 토큰입니다", "401"),
    AUTH_TOKEN_ILLEGAL("claims 정보가 비어있습니다", "401"),
    AUTH_TOKEN_NOT_SIGNATURE("Jwt 서명이 로컬로 산정된 서명과 일치하지 않습니다", "401");

    private final String message;
    private final String code;

}
