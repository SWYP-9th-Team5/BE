package swyp.team5.greening.petPlant.dto.request;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import swyp.team5.greening.petPlant.dto.DailyRecordContentDto;

public record UpdateDailyRecordRequestDto(

        @NotEmpty
        String title,

        @NotEmpty
        List<DailyRecordContentDto> content
) {

}
