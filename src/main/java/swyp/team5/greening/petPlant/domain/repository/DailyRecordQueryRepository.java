package swyp.team5.greening.petPlant.domain.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import swyp.team5.greening.petPlant.domain.entity.DailyRecord;
import swyp.team5.greening.petPlant.dto.data.FindDailyRecordDto;

public interface DailyRecordQueryRepository {

    List<DailyRecord> findByPetPlantAndWriteDate(
            Long petPlantId,
            LocalDate startDate,
            LocalDate endDate
    );

    Optional<FindDailyRecordDto> findDailyRecord(Long dailyRecordId);
}
