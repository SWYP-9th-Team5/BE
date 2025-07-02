package swyp.team5.greening.petPlant.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import swyp.team5.greening.petPlant.domain.entity.Watering;
import swyp.team5.greening.petPlant.domain.repository.WateringRepository;

public interface WateringJpaRepository extends JpaRepository<Watering, Long>, WateringRepository {

}
