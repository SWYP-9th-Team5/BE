package swyp.team5.greening.petPlant.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.team5.greening.common.exception.GreeningGlobalException;
import swyp.team5.greening.petPlant.domain.entity.DailyRecord;
import swyp.team5.greening.petPlant.domain.entity.PetPlant;
import swyp.team5.greening.petPlant.domain.entity.PetPlantState;
import swyp.team5.greening.petPlant.domain.entity.Watering;
import swyp.team5.greening.petPlant.domain.repository.DailyRecordQueryRepository;
import swyp.team5.greening.petPlant.domain.repository.PetPlantQueryRepository;
import swyp.team5.greening.petPlant.domain.repository.PetPlantRepository;
import swyp.team5.greening.petPlant.domain.repository.WateringQueryRepository;
import swyp.team5.greening.petPlant.dto.response.FindAllPetPlantResponseDto;
import swyp.team5.greening.petPlant.dto.response.FindPetPlantDateResponseDto;
import swyp.team5.greening.petPlant.exception.PetPlantExceptionMessage;

@Service
@RequiredArgsConstructor
public class PetPlantQueryService {

    private final PetPlantRepository petPlantRepository;
    private final PetPlantQueryRepository petPlantQueryRepository;

    private final DailyRecordQueryRepository dailyRecordQueryRepository;
    private final WateringQueryRepository wateringQueryRepository;

    //나의 애완 식물 목록 조회
    @Transactional(readOnly = true)
    public List<FindAllPetPlantResponseDto> findMyPetPlants(Long userId) {
        return petPlantQueryRepository.findMyPetPlants(userId);
    }

    //특정 애완 식물 특정 월 정보 조회
    //1. 애완 식물 조회
    //2. 사용자 유효성 검증
    //3. 해당 월에 대한 물 주기 스탬프 목록 조회
    //4. 해당 월에 대한 오늘의 일기 식별자 조회
    //5. 조합
    @Transactional(readOnly = true)
    public List<FindPetPlantDateResponseDto> findMyPetPlantCalender(
            Long userId,
            Long petPlantId,
            Integer year,
            Integer month
    ) {
        //1
        PetPlant petPlant = petPlantRepository.findByIdAndState(petPlantId,
                        PetPlantState.IN_PROGRESS)
                .orElseThrow(() -> new GreeningGlobalException(
                        PetPlantExceptionMessage.NOT_FOUND_PET_PLANT));

        //2
        if (!Objects.equals(petPlant.getUserId(), userId)) {
            throw new GreeningGlobalException(
                    PetPlantExceptionMessage.BAD_REQUEST_PET_PLANT_WRITER);
        }

        YearMonth requestDate = YearMonth.of(year, month);

        LocalDate start = requestDate.atDay(1);
        LocalDate end = requestDate.plusMonths(1).atDay(1);

        //3
        List<DailyRecord> dailyRecordsByWriteDate = dailyRecordQueryRepository
                .findByPetPlantAndWriteDate(petPlantId, start, end);

        //4
        List<Watering> wateringByWriteDate = wateringQueryRepository
                .findByPetPlantAndWriteDate(petPlantId, start, end);

        //5
        //날짜 별 일기 정보 조회
        Map<LocalDate, Long> dailyRecordIdMap = dailyRecordsByWriteDate.stream()
                .collect(Collectors.toMap(DailyRecord::getWriteDate, DailyRecord::getId));

        //날짜 별 물 주기 스탬프 조회
        Set<LocalDate> wateredDates = wateringByWriteDate.stream()
                .map(Watering::getWriteDate)
                .collect(Collectors.toSet());

        //해당 월에 대한 정보 존재 날짜 집합 정리
        Set<LocalDate> allDates = new HashSet<>();
        allDates.addAll(dailyRecordIdMap.keySet());
        allDates.addAll(wateredDates);

        //조합
        return allDates.stream()
                .map(date -> {
                    Long dailyRecordId = dailyRecordIdMap.getOrDefault(date, -1L);
                    boolean isWatered = wateredDates.contains(date);
                    return new FindPetPlantDateResponseDto(date, isWatered, dailyRecordId);
                })
                .sorted(Comparator.comparing(FindPetPlantDateResponseDto::date)) // 오름차순 정렬
                .toList();
    }

}
