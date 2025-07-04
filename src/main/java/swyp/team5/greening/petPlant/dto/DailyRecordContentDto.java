package swyp.team5.greening.petPlant.dto;

import jakarta.validation.constraints.NotEmpty;

public record DailyRecordContentDto(
        @NotEmpty
        String type,

        @NotEmpty
        String value
) {

}
