package swyp.team5.greening.petPlant.infrastructure;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import swyp.team5.greening.petPlant.domain.entity.DailyRecord;
import swyp.team5.greening.petPlant.domain.repository.DailyRecordQueryRepository;
import swyp.team5.greening.petPlant.dto.data.FindDailyRecordDto;

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

    @Override
    @Query("""
            SELECT new swyp.team5.greening.petPlant.dto.data.FindDailyRecordDto(
                        dailyRecord, petPlant)
            FROM DailyRecord dailyRecord
            INNER JOIN PetPlant petPlant
            ON petPlant.id = dailyRecord.petPlantId
            WHERE dailyRecord.id = :dailyRecordId
            AND dailyRecord.state = 'IN_PROGRESS'
            """)
    Optional<FindDailyRecordDto> findDailyRecord(@Param("dailyRecordId") Long dailyRecordId);

}
