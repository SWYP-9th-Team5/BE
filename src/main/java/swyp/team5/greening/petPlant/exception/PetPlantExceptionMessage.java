package swyp.team5.greening.petPlant.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import swyp.team5.greening.common.exception.ExceptionMessage;

@Getter
@RequiredArgsConstructor
public enum PetPlantExceptionMessage implements ExceptionMessage {

    NOT_FOUND_PET_PLANT("존재하지 않는 애완 식물입니다.", "404"),
    BAD_REQUEST_PET_PLANT_WRITER("내가 등록한 애완 식물이 아닙니다.", "400");

    private final String message;
    private final String code;

}
