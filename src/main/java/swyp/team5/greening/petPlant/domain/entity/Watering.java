package swyp.team5.greening.petPlant.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swyp.team5.greening.common.base.BaseTimeEntity;

@Entity
@Table(name = "watering")
@Getter
@NoArgsConstructor
public class Watering extends BaseTimeEntity {

    @Id
    @Column(name = "watering_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pet_plant_id")
    private Long petPlantId;

    @Column(name = "write_date")
    private LocalDate writeDate;

    @Builder
    public Watering(
            Long petPlantId,
            LocalDate writeDate
    ) {
        this.petPlantId = petPlantId;
        this.writeDate = writeDate;
    }
}
