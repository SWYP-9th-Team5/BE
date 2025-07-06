package swyp.team5.greening.petPlant.domain.repository;

import java.time.LocalDate;
import java.util.List;
import swyp.team5.greening.petPlant.domain.entity.DailyRecord;

public interface DailyRecordQueryRepository {

    List<DailyRecord> findByPetPlantAndWriteDate(
            Long petPlantId,
            LocalDate startDate,
            LocalDate endDate
    );
}
