package swyp.team5.greening.petPlant.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.team5.greening.petPlant.domain.repository.PetPlantQueryRepository;
import swyp.team5.greening.petPlant.dto.response.FindAllPetPlantResponseDto;

@Service
@RequiredArgsConstructor
public class PetPlantQueryService {

    private final PetPlantQueryRepository petPlantQueryRepository;

    @Transactional(readOnly = true)
    public List<FindAllPetPlantResponseDto> findMyPetPlants(Long userId) {
        return petPlantQueryRepository.findMyPetPlants(userId);
    }

}
