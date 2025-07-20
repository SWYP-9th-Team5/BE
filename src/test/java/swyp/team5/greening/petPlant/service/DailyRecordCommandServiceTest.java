package swyp.team5.greening.petPlant.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.BDDMockito.given;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import swyp.team5.greening.common.exception.GreeningGlobalException;
import swyp.team5.greening.petPlant.domain.entity.DailyRecord;
import swyp.team5.greening.petPlant.domain.entity.PetPlant;
import swyp.team5.greening.petPlant.domain.entity.PetPlantState;
import swyp.team5.greening.petPlant.domain.repository.DailyRecordRepository;
import swyp.team5.greening.petPlant.domain.repository.PetPlantRepository;
import swyp.team5.greening.petPlant.dto.DailyRecordContentDto;
import swyp.team5.greening.petPlant.dto.request.CreateDailyRecordRequestDto;
import swyp.team5.greening.petPlant.dto.response.CreateDailyRecordResponseDto;
import swyp.team5.greening.petPlant.exception.PetPlantExceptionMessage;

@ExtendWith(MockitoExtension.class)
@DisplayName("DailyRecordCommandService 테스트")
class DailyRecordCommandServiceTest {

    @Mock
    private DailyRecordRepository dailyRecordRepository;

    @Mock
    private PetPlantRepository petPlantRepository;

    @Mock
    private Supplier<LocalDate> now;

    @InjectMocks
    private DailyRecordCommandService dailyRecordCommandService;

    @Nested
    @DisplayName("사용자는 애완 식물 1개를 등록한 상태이다.")
    class TestCase1 {

        private final Long userId = 1L;
        private final Long anotherUserId = 3L;
        private final String title = "오늘의 일기";
        private final String content = "오늘은 어쩌구저쩌구";

        private final LocalDate nowDate = LocalDate.of(2025, 7, 3);
        private final LocalDate future = LocalDate.of(2030, 8, 16);

        private PetPlant petPlant;

        @BeforeEach
        void init() {
            petPlant = PetPlant.builder()
                    .name("귀요미")
                    .plantType("민들레")
                    .state(PetPlantState.IN_PROGRESS)
                    .userId(userId)
                    .build();

            ReflectionTestUtils.setField(petPlant, "id", 10L);
            given(petPlantRepository.findByIdAndState(petPlant.getId(), PetPlantState.IN_PROGRESS))
                    .willReturn(Optional.of(petPlant));
        }

        @Test
        @DisplayName("사용자는 자신이 키우는 애완 식물에 대해 오늘의 기록을 작성할 수 있다.")
        void createDailyRecord1() {
            //given
            DailyRecord dailyRecord = DailyRecord.builder()
                    .title(title)
                    .writeDate(nowDate)
                    .petPlantId(petPlant.getId())
                    .build();
            ReflectionTestUtils.setField(dailyRecord, "id", 20L);

            given(dailyRecordRepository.save(any(DailyRecord.class))).willReturn(dailyRecord);

            List<DailyRecordContentDto> contents = List.of(
                    new DailyRecordContentDto("TEXT", content));

            given(now.get()).willReturn(nowDate);

            //when
            CreateDailyRecordResponseDto result = dailyRecordCommandService.createDailyRecord(
                    userId, petPlant.getId(),
                    new CreateDailyRecordRequestDto(nowDate, title,
                            contents));

            //then
            assertThat(result.dailyRecordId()).isEqualTo(dailyRecord.getId());
            verify(dailyRecordRepository, times(1)).save(any(DailyRecord.class));
        }

        @Test
        @DisplayName("다른 사용자는 애완 식물에 대해 오늘의 기록을 작성할 수 없다.")
        void createDailyRecord2() {
            //given
            List<DailyRecordContentDto> contents = List.of(
                    new DailyRecordContentDto("TEXT", content));

            //when
            ThrowingCallable throwingCallable = () -> dailyRecordCommandService.createDailyRecord(
                    anotherUserId, petPlant.getId(),
                    new CreateDailyRecordRequestDto(nowDate, title,
                            contents));

            //then
            assertThatThrownBy(throwingCallable)
                    .isInstanceOf(GreeningGlobalException.class)
                    .hasMessage(PetPlantExceptionMessage.BAD_REQUEST_PET_PLANT_WRITER.getMessage());
        }

        @Test
        @DisplayName("오늘보다 이후 날짜에 대해 오늘의 기록을 작성할 수 없다.")
        void createDailyRecord3() {
            //given
            List<DailyRecordContentDto> contents = List.of(
                    new DailyRecordContentDto("TEXT", content));

            given(now.get()).willReturn(nowDate);

            //when
            ThrowingCallable throwingCallable = () -> dailyRecordCommandService.createDailyRecord(
                    userId, petPlant.getId(),
                    new CreateDailyRecordRequestDto(future, title,
                            contents));

            //then
            assertThatThrownBy(throwingCallable)
                    .isInstanceOf(GreeningGlobalException.class)
                    .hasMessage(PetPlantExceptionMessage.INVALID_DATE_ACCESS.getMessage());
        }
    }

}
