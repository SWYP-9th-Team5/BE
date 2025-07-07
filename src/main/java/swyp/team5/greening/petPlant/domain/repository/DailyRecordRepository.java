package swyp.team5.greening.petPlant.domain.repository;

import java.time.LocalDate;
import java.util.Optional;
import swyp.team5.greening.petPlant.domain.entity.DailyRecord;
import swyp.team5.greening.petPlant.domain.entity.DailyRecordState;

public interface DailyRecordRepository {

    DailyRecord save(DailyRecord dailyRecord);

    boolean existsByPetPlantIdAndWriteDateAndState(Long petPlantId, LocalDate writeDate, DailyRecordState state);

    Optional<DailyRecord> findByIdAndState(Long dailyRecordId, DailyRecordState state);

    void deleteAll();
}
