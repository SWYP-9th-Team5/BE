package swyp.team5.greening.petPlant.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swyp.team5.greening.common.base.BaseTimeEntity;

@Entity
@Table(name = "pet_plants")
@Getter
@NoArgsConstructor
public class PetPlant extends BaseTimeEntity {

    @Id
    @Column(name = "pet_plant_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "plant_type")
    private String plantType;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private PetPlantState state;

    @Column(name = "user_id")
    private Long userId;

    @Builder
    public PetPlant(
            String name,
            String plantType,
            PetPlantState state,
            Long userId
    ) {
        this.name = name;
        this.plantType = plantType;
        this.state = state;
        this.userId = userId;
    }

    public void deletePetPlant() {
        this.state = PetPlantState.DELETED;
    }
}
