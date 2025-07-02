package swyp.team5.greening.petPlant.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record CreatePetPlantRequestDto(
        @NotEmpty
        String name,

        @NotEmpty
        String type
) {

}
