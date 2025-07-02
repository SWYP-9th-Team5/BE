package swyp.team5.greening.petPlant.domain.repository;

import java.util.List;
import swyp.team5.greening.petPlant.dto.response.FindAllPetPlantResponseDto;

public interface PetPlantQueryRepository {

    List<FindAllPetPlantResponseDto> findMyPetPlants(Long loginUserId);

}
