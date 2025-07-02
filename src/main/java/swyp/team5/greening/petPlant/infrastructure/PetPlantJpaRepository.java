package swyp.team5.greening.petPlant.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import swyp.team5.greening.petPlant.domain.entity.PetPlant;
import swyp.team5.greening.petPlant.domain.repository.PetPlantRepository;

public interface PetPlantJpaRepository extends JpaRepository<PetPlant, Long>, PetPlantRepository {

}
