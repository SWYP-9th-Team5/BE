package swyp.team5.greening.petPlant.dto.data;

import swyp.team5.greening.petPlant.domain.entity.DailyRecord;
import swyp.team5.greening.petPlant.domain.entity.PetPlant;

public record FindDailyRecordDto(
        DailyRecord dailyRecord,
        PetPlant petPlant
) {

}
