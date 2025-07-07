package swyp.team5.greening.petPlant.domain.repository;

import java.util.Optional;
import swyp.team5.greening.petPlant.domain.entity.DailyRecord;
import swyp.team5.greening.petPlant.domain.entity.DailyRecordState;

public interface DailyRecordRepository {

    DailyRecord save(DailyRecord dailyRecord);

    Optional<DailyRecord> findByIdAndState(Long dailyRecordId, DailyRecordState state);

    void deleteAll();
}
