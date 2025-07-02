package swyp.team5.greening.petPlant.dto.response;

import java.time.LocalDateTime;

public record FindAllPetPlantResponseDto(
        Long id,
        String name,
        String type,
        LocalDateTime createdAt
) {

}
