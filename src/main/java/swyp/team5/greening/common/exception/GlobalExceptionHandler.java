package swyp.team5.greening.common.exception;

import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final String ARGUMENT_NOT_VALID_MESSAGE = "잘못된 입력 값입니다.";

    // Validation 예외를 처리하는 핸들러
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception
    ) {
        String message = Objects.requireNonNullElse(
                exception.getBindingResult()
                        .getAllErrors()
                        .get(0).getDefaultMessage(),
                ARGUMENT_NOT_VALID_MESSAGE);

        log.error("HttpStatus : {}, Exception Message : {}",
                exception.getStatusCode(),
                message);

        return new ResponseEntity<>(new ExceptionResponse(message), exception.getStatusCode());
    }

    //커스텀 예외 처리 핸들러
    @ExceptionHandler(GreeningGlobalException.class)
    public ResponseEntity<ExceptionResponse> globalExceptionHandler(
            GreeningGlobalException exception
    ) {
        String message = exception.getMessage();
        String code = exception.getCode();

        log.error("HttpStatus : {}, Exception Message : {}", code, message);

        return new ResponseEntity<>(new ExceptionResponse(message),
                HttpStatusCode.valueOf(Integer.parseInt(code)));
    }
}
