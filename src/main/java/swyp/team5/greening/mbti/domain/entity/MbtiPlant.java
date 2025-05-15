package swyp.team5.greening.mbti.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import swyp.team5.greening.common.base.BaseTimeEntity;

@Entity
@Table(name = "mbti_plants")
@Getter
public class MbtiPlant extends BaseTimeEntity {

    @Id
    @Column(name = "plant_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "plant_name")
    private String plantName;

    @Column(name = "plant_description")
    private String plantDescription;

    @Column(name = "plant_personality")
    private String plantPersonality;

    @Column(name = "suitable_plant")
    private String suitablePlant;

    @Column(name = "unsuitable_plant")
    private String unsuitablePlant;

    @Column(name = "recommended_plant")
    private String recommendedPlant;

    @OneToMany(mappedBy = "mbtiPlant", cascade = {CascadeType.PERSIST}, orphanRemoval = true)
    private final List<PlantImage> plantImages = new ArrayList<>();
}
