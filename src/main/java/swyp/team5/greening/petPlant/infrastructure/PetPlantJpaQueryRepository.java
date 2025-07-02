package swyp.team5.greening.petPlant.infrastructure;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import swyp.team5.greening.petPlant.domain.entity.PetPlant;
import swyp.team5.greening.petPlant.domain.repository.PetPlantQueryRepository;
import swyp.team5.greening.petPlant.dto.response.FindAllPetPlantResponseDto;

public interface PetPlantJpaQueryRepository extends JpaRepository<PetPlant, Long>,
        PetPlantQueryRepository {

    @Override
    @Query("""
            SELECT new swyp.team5.greening.petPlant.dto.response.FindAllPetPlantResponseDto(
            petPlant.id, petPlant.name, petPlant.plantType, petPlant.createdAt)
            FROM PetPlant petPlant
            WHERE petPlant.userId = :loginUserId
            AND petPlant.state = 'IN_PROGRESS'
            """)
    List<FindAllPetPlantResponseDto> findMyPetPlants(
            @Param("loginUserId") Long loginUserId
    );

}
