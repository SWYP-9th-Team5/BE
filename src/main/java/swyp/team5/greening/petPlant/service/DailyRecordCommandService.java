package swyp.team5.greening.petPlant.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.team5.greening.common.exception.GreeningGlobalException;
import swyp.team5.greening.petPlant.domain.entity.DailyRecord;
import swyp.team5.greening.petPlant.domain.entity.DailyRecordContent;
import swyp.team5.greening.petPlant.domain.entity.DailyRecordContentType;
import swyp.team5.greening.petPlant.domain.entity.DailyRecordState;
import swyp.team5.greening.petPlant.domain.entity.PetPlant;
import swyp.team5.greening.petPlant.domain.entity.PetPlantState;
import swyp.team5.greening.petPlant.domain.repository.DailyRecordRepository;
import swyp.team5.greening.petPlant.domain.repository.PetPlantRepository;
import swyp.team5.greening.petPlant.dto.request.CreateDailyRecordRequestDto;
import swyp.team5.greening.petPlant.dto.response.CreateDailyRecordResponseDto;
import swyp.team5.greening.petPlant.exception.PetPlantExceptionMessage;

@Service
@RequiredArgsConstructor
public class DailyRecordCommandService {

    private final Supplier<LocalDate> nowDate;

    private final PetPlantRepository petPlantRepository;
    private final DailyRecordRepository dailyRecordRepository;

    //오늘의 기록 작성
    //1. 애완 식물 조회
    //2. 사용자 유효성 검증
    //3. 오늘 날짜 여부 확인
    //4. 오늘의 기록 저장
    @Transactional
    public CreateDailyRecordResponseDto createDailyRecord(
            Long userId,
            Long petPlantId,
            CreateDailyRecordRequestDto requestDto
    ) {
        PetPlant petPlant = petPlantRepository.findByIdAndState(petPlantId,
                        PetPlantState.IN_PROGRESS)
                .orElseThrow(() -> new GreeningGlobalException(
                        PetPlantExceptionMessage.NOT_FOUND_PET_PLANT));

        //2
        if (!Objects.equals(petPlant.getUserId(), userId)) {
            throw new GreeningGlobalException(
                    PetPlantExceptionMessage.BAD_REQUEST_PET_PLANT_WRITER);
        }

        //3
        if (!Objects.equals(nowDate.get(), requestDto.today())) {
            throw new GreeningGlobalException(PetPlantExceptionMessage.INVALID_DATE_ACCESS);
        }

        //4
        DailyRecord dailyRecord = DailyRecord.builder()
                .title(requestDto.title())
                .writeDate(requestDto.today())
                .state(DailyRecordState.IN_PROGRESS)
                .petPlantId(petPlantId)
                .build();

        List<DailyRecordContent> contents = requestDto.content().stream()
                .map(dto -> DailyRecordContent.builder()
                        .content(dto.value())
                        .type(DailyRecordContentType.valueOf(dto.type()))
                        .build())
                .toList();

        dailyRecord.updateContent(contents);

        DailyRecord saved = dailyRecordRepository.save(dailyRecord);

        return new CreateDailyRecordResponseDto(saved.getId());
    }

}
