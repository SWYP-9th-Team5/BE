package swyp.team5.greening.mbti.dto.request;

import jakarta.validation.constraints.NotBlank;

public record FindMbtiResultRequestDto(
        @NotBlank
        String mbti
) {

}
