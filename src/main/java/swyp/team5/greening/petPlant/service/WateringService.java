package swyp.team5.greening.petPlant.service;

import java.time.LocalDate;
import java.util.Objects;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.team5.greening.common.exception.GreeningGlobalException;
import swyp.team5.greening.petPlant.domain.entity.PetPlant;
import swyp.team5.greening.petPlant.domain.entity.PetPlantState;
import swyp.team5.greening.petPlant.domain.entity.Watering;
import swyp.team5.greening.petPlant.domain.repository.PetPlantRepository;
import swyp.team5.greening.petPlant.domain.repository.WateringRepository;
import swyp.team5.greening.petPlant.dto.response.WateringPlantResponseDto;
import swyp.team5.greening.petPlant.exception.PetPlantExceptionMessage;

@Service
@RequiredArgsConstructor
public class WateringService {

    private final Supplier<LocalDate> nowDate;

    private final PetPlantRepository petPlantRepository;
    private final WateringRepository wateringRepository;

    //애완 식물 물 주기 스탬프 등록
    //1. 애완 식물 조회
    //2. 사용자 유효성 검증
    //3. 오늘 날짜 여부 확인
    //4. 물 주기 스탬프 등록
    @Transactional
    public WateringPlantResponseDto wateringPlant(
            Long userId,
            Long petPlantId,
            LocalDate today
    ) {
        PetPlant petPlant = petPlantRepository.findByIdAndState(petPlantId,
                        PetPlantState.IN_PROGRESS)
                .orElseThrow(() -> new GreeningGlobalException(
                        PetPlantExceptionMessage.NOT_FOUND_PET_PLANT));

        if (!Objects.equals(petPlant.getUserId(), userId)) {
            throw new GreeningGlobalException(
                    PetPlantExceptionMessage.BAD_REQUEST_PET_PLANT_WRITER);
        }

        if (!Objects.equals(nowDate.get(), today)) {
            throw new GreeningGlobalException(PetPlantExceptionMessage.INVALID_DATE_ACCESS);
        }

        Watering watering = Watering.builder()
                .writeDate(today)
                .petPlantId(petPlantId)
                .build();
        Watering savedWatering = wateringRepository.save(watering);

        return new WateringPlantResponseDto(savedWatering.getId());
    }

}
