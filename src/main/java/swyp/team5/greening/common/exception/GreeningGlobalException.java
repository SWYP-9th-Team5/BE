package swyp.team5.greening.common.exception;

import lombok.Getter;

@Getter
public class GreeningGlobalException extends RuntimeException {

    private final String code;

    public GreeningGlobalException(ExceptionMessage message) {
        super(message.getMessage());
        this.code = message.getCode();
    }

}
