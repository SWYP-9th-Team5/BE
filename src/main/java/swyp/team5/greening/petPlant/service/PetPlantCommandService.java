package swyp.team5.greening.petPlant.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.team5.greening.petPlant.domain.entity.PetPlant;
import swyp.team5.greening.petPlant.domain.entity.PetPlantState;
import swyp.team5.greening.petPlant.domain.repository.PetPlantRepository;
import swyp.team5.greening.petPlant.dto.request.CreatePetPlantRequestDto;
import swyp.team5.greening.petPlant.dto.response.CreatePetPlantResponseDto;

@Service
@RequiredArgsConstructor
public class PetPlantCommandService {

    private final PetPlantRepository petPlantRepository;

    //애완 식물 생성
    @Transactional
    public CreatePetPlantResponseDto createPetPlant(
            Long userId,
            CreatePetPlantRequestDto requestDto
    ) {
        PetPlant petPlant = PetPlant.builder()
                .name(requestDto.name())
                .plantType(requestDto.type())
                .state(PetPlantState.IN_PROGRESS)
                .userId(userId)
                .build();

        petPlantRepository.save(petPlant);

        return new CreatePetPlantResponseDto(petPlant.getId());
    }

}
