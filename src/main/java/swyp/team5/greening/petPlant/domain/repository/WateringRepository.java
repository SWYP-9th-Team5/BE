package swyp.team5.greening.petPlant.domain.repository;

import java.util.List;
import java.util.Optional;
import swyp.team5.greening.petPlant.domain.entity.Watering;

public interface WateringRepository {

    Watering save(Watering watering);

    List<Watering> findAll();

    void deleteAll();
}
