package swyp.team5.greening.petPlant.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swyp.team5.greening.common.base.BaseTimeEntity;

@Entity
@Table(name = "daily_records")
@Getter
@NoArgsConstructor
public class DailyRecord extends BaseTimeEntity {

    @Id
    @Column(name = "daily_record_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "write_date")
    private LocalDate writeDate;

    @Column(name = "pet_plant_id")
    private Long petPlantId;

    @OneToMany(mappedBy = "dailyRecord", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private final List<DailyRecordContent> dailyRecordContents = new ArrayList<>();

}
