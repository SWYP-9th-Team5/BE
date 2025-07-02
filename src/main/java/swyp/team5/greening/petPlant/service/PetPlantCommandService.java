package swyp.team5.greening.petPlant.service;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.team5.greening.common.exception.GreeningGlobalException;
import swyp.team5.greening.petPlant.domain.entity.PetPlant;
import swyp.team5.greening.petPlant.domain.entity.PetPlantState;
import swyp.team5.greening.petPlant.domain.repository.PetPlantRepository;
import swyp.team5.greening.petPlant.dto.request.CreatePetPlantRequestDto;
import swyp.team5.greening.petPlant.dto.response.CreatePetPlantResponseDto;
import swyp.team5.greening.petPlant.exception.PetPlantExceptionMessage;

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

    //애완 식물 삭제
    @Transactional
    public void deletePetPlant(
            Long userId,
            Long petPlantId
    ) {
        PetPlant petPlant = petPlantRepository.findByIdAndState(petPlantId,
                        PetPlantState.IN_PROGRESS)
                .orElseThrow(() -> new GreeningGlobalException(
                        PetPlantExceptionMessage.NOT_FOUND_PET_PLANT));

        if (!Objects.equals(petPlant.getUserId(), userId)) {
            throw new GreeningGlobalException(
                    PetPlantExceptionMessage.BAD_REQUEST_PET_PLANT_WRITER);
        }

        petPlant.deletePetPlant();
    }

}
