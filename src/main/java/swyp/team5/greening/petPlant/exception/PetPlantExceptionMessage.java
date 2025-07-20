package swyp.team5.greening.petPlant.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import swyp.team5.greening.common.exception.ExceptionMessage;

@Getter
@RequiredArgsConstructor
public enum PetPlantExceptionMessage implements ExceptionMessage {

    NOT_FOUND_PET_PLANT("존재하지 않는 애완 식물입니다.", "404"),
    BAD_REQUEST_PET_PLANT_WRITER("내가 등록한 애완 식물이 아닙니다.", "400"),

    NOT_FOUND_DAILY_RECORD("해당 날짜의 기록이 존재하지 않습니다.", "404"),
    ALREADY_EXISTS_DAILY_RECORD("이미 작성한 오늘의 기록이 존재합니다", "409"),

    INVALID_DATE_ACCESS("해당 날짜에 미리 접근할 수 없습니다.", "400");

    private final String message;
    private final String code;

}
