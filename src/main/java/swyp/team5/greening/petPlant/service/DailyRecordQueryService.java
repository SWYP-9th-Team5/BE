package swyp.team5.greening.petPlant.service;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.team5.greening.common.exception.GreeningGlobalException;
import swyp.team5.greening.petPlant.domain.entity.DailyRecord;
import swyp.team5.greening.petPlant.domain.entity.PetPlant;
import swyp.team5.greening.petPlant.domain.repository.DailyRecordQueryRepository;
import swyp.team5.greening.petPlant.dto.data.FindDailyRecordDto;
import swyp.team5.greening.petPlant.dto.response.FindDailyRecordResponseDto;
import swyp.team5.greening.petPlant.exception.PetPlantExceptionMessage;

@Service
@RequiredArgsConstructor
public class DailyRecordQueryService {

    private final DailyRecordQueryRepository dailyRecordQueryRepository;

    @Transactional
    public FindDailyRecordResponseDto findDailyRecord(
            Long userId,
            Long dailyRecordId
    ) {
        FindDailyRecordDto dailyRecordDto = dailyRecordQueryRepository.findDailyRecord(
                        dailyRecordId)
                .orElseThrow(()
                        -> new GreeningGlobalException(PetPlantExceptionMessage.NOT_FOUND_DAILY_RECORD));

        DailyRecord dailyRecord = dailyRecordDto.dailyRecord();
        PetPlant petPlant = dailyRecordDto.petPlant();

        if (!Objects.equals(petPlant.getUserId(), userId)) {
            throw new GreeningGlobalException(PetPlantExceptionMessage.BAD_REQUEST_PET_PLANT_WRITER);
        }

        return FindDailyRecordResponseDto.of(dailyRecord);
    }

}
