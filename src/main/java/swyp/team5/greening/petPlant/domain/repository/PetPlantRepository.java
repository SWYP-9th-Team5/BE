package swyp.team5.greening.petPlant.domain.repository;

import java.util.Optional;
import swyp.team5.greening.petPlant.domain.entity.PetPlant;
import swyp.team5.greening.petPlant.domain.entity.PetPlantState;

public interface PetPlantRepository {

    PetPlant save(PetPlant petPlant);

    Optional<PetPlant> findByIdAndState(Long id, PetPlantState state);

    void deleteAll();
}
