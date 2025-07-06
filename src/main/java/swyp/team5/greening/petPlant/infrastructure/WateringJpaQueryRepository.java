package swyp.team5.greening.petPlant.infrastructure;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import swyp.team5.greening.petPlant.domain.entity.Watering;
import swyp.team5.greening.petPlant.domain.repository.WateringQueryRepository;

public interface WateringJpaQueryRepository extends JpaRepository<Watering, Long>,
        WateringQueryRepository {

    @Override
    @Query("""
            SELECT watering
            FROM Watering watering
            WHERE watering.petPlantId = :petPlantId
            AND watering.writeDate >= :startDate
            AND watering.writeDate < :endDate
            ORDER BY watering.writeDate asc
            """)
    List<Watering> findByPetPlantAndWriteDate(
            @Param("petPlantId") Long petPlantId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

}
