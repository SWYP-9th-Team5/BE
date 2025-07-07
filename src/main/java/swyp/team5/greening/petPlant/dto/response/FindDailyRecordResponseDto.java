package swyp.team5.greening.petPlant.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import swyp.team5.greening.petPlant.domain.entity.DailyRecord;
import swyp.team5.greening.petPlant.domain.entity.DailyRecordContent;
import swyp.team5.greening.petPlant.dto.DailyRecordContentDto;

public record FindDailyRecordResponseDto(

        String title,

        @JsonFormat(pattern = "yyyy-MM-dd'T'hh:mm:ss")
        LocalDateTime createdAt,

        List<DailyRecordContentDto> content

) {

    public static FindDailyRecordResponseDto of(DailyRecord dailyRecord) {

        List<DailyRecordContent> dailyRecordContents = dailyRecord.getDailyRecordContents();

        return new FindDailyRecordResponseDto(
                dailyRecord.getTitle(),
                dailyRecord.getCreatedAt(),
                dailyRecordContents.stream()
                        .sorted(Comparator.comparing(DailyRecordContent::getSequence))
                        .map(content ->
                                new DailyRecordContentDto(content.getType().name(),
                                        content.getContent()))
                        .toList()
        );

    }

}
