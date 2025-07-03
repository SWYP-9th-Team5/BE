package swyp.team5.greening.petPlant.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record WateringPlantRequestDto(
        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate today
) {

}
