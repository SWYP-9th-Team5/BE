package swyp.team5.greening.petPlant.dto.request;

import jakarta.validation.constraints.NotNull;

public record FindPetPlantDateRequestDto(
        @NotNull
        Integer year,

        @NotNull
        Integer month
) {

}
