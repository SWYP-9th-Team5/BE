package swyp.team5.greening.petPlant.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import swyp.team5.greening.petPlant.dto.DailyRecordContentDto;

public record CreateDailyRecordRequestDto(

        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate today,

        @NotEmpty
        String title,

        @NotEmpty
        List<DailyRecordContentDto> content
) {

}
