package swyp.team5.greening.petPlant.infrastructure;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import swyp.team5.greening.petPlant.domain.entity.DailyRecord;
import swyp.team5.greening.petPlant.domain.repository.DailyRecordQueryRepository;

public interface DailyRecordJpaQueryRepository extends JpaRepository<DailyRecord, Long>,
        DailyRecordQueryRepository {

    @Override
    @Query("""
            SELECT dailyRecord
            FROM DailyRecord dailyRecord
            WHERE dailyRecord.petPlantId = :petPlantId
            AND dailyRecord.writeDate >= :startDate
            AND dailyRecord.writeDate < :endDate
            AND dailyRecord.state = 'IN_PROGRESS'
            ORDER BY dailyRecord.writeDate asc
            """)
    List<DailyRecord> findByPetPlantAndWriteDate(
            @Param("petPlantId") Long petPlantId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

}
