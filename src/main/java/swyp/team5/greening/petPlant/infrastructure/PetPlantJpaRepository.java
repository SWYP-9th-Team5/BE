package swyp.team5.greening.petPlant.infrastructure;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import swyp.team5.greening.petPlant.domain.entity.PetPlant;
import swyp.team5.greening.petPlant.domain.entity.PetPlantState;
import swyp.team5.greening.petPlant.domain.repository.PetPlantRepository;

public interface PetPlantJpaRepository extends JpaRepository<PetPlant, Long>, PetPlantRepository {

    @Override
    Optional<PetPlant> findByIdAndState(Long id, PetPlantState state);

}
