package swyp.team5.greening.mbti.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import swyp.team5.greening.common.base.BaseTimeEntity;

@Entity
@Table(name = "mbti")
@Getter
public class Mbti extends BaseTimeEntity {

    @Id
    @Column(name = "mbti_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "mbti_type")
    private MbtiType mbtiType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_id")
    private MbtiPlant plant;
}
