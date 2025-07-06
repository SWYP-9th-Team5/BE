package swyp.team5.greening.petPlant.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public record FindPetPlantDateResponseDto(
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,

        Boolean watering,

        Long dailyRecordId
) {

}
