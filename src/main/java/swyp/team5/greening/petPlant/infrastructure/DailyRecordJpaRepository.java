package swyp.team5.greening.petPlant.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import swyp.team5.greening.petPlant.domain.entity.DailyRecord;
import swyp.team5.greening.petPlant.domain.repository.DailyRecordRepository;

public interface DailyRecordJpaRepository extends JpaRepository<DailyRecord, Long>,
        DailyRecordRepository {

}
