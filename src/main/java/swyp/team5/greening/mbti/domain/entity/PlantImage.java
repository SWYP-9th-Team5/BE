package swyp.team5.greening.mbti.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import swyp.team5.greening.common.base.BaseTimeEntity;

@Entity
@Table(name = "plant_images")
public class PlantImage extends BaseTimeEntity {

    @Id
    @Column(name = "plant_image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "plant_image_url")
    private String plantImageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_id")
    private MbtiPlant mbtiPlant;

}
