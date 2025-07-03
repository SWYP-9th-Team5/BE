package swyp.team5.greening.petPlant.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
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
import swyp.team5.greening.petPlant.domain.entity.PetPlant;
import swyp.team5.greening.petPlant.domain.entity.PetPlantState;
import swyp.team5.greening.petPlant.domain.entity.Watering;
import swyp.team5.greening.petPlant.domain.repository.PetPlantRepository;
import swyp.team5.greening.petPlant.domain.repository.WateringRepository;
import swyp.team5.greening.petPlant.dto.response.WateringPlantResponseDto;
import swyp.team5.greening.petPlant.exception.PetPlantExceptionMessage;

@ExtendWith(MockitoExtension.class)
@DisplayName("WateringService 테스트")
class WateringServiceTest {

    @Mock
    private WateringRepository wateringRepository;

    @Mock
    private PetPlantRepository petPlantRepository;

    @Mock
    private Supplier<LocalDate> now;

    @InjectMocks
    private WateringService wateringService;

    @Nested
    @DisplayName("사용자는 애완 식물 1개를 등록한 상태이다.")
    class TestCase1 {
        private final Long userId = 1L;
        private final Long anotherUserId = 3L;
        private final String name = "귀요미";
        private final String type = "민들레";

        private final LocalDate nowDate = LocalDate.of(2025,7,3);
        private final LocalDate future = LocalDate.of(2030,8,16);

        private PetPlant petPlant;

        @BeforeEach
        void init() {
            petPlant = PetPlant.builder()
                    .name(name)
                    .plantType(type)
                    .state(PetPlantState.IN_PROGRESS)
                    .userId(userId)
                    .build();

            ReflectionTestUtils.setField(petPlant, "id", 10L);
            given(petPlantRepository.findByIdAndState(petPlant.getId(), PetPlantState.IN_PROGRESS))
                    .willReturn(Optional.of(petPlant));
        }

        @Test
        @DisplayName("사용자가 자신이 키우는 애완 식물의 물 주기 스탬프를 줄 수 있다.")
        void wateringPlantTest() {
            //given
            given(now.get()).willReturn(nowDate);

            Watering watering = Watering.builder()
                    .petPlantId(petPlant.getId())
                    .writeDate(nowDate)
                    .build();
            ReflectionTestUtils.setField(watering, "id", 1L);
            given(wateringRepository.save(any(Watering.class))).willReturn(watering);

            //when
            WateringPlantResponseDto result = wateringService.wateringPlant(
                    userId, petPlant.getId(), nowDate);

            //then
            assertThat(1L).isEqualTo(result.wateringId());
            verify(wateringRepository, times(1)).save(any(Watering.class));
        }

        @Test
        @DisplayName("자신의 애완 식물이 아닐 경우, 물 주기 스탬프를 줄 수 없다.")
        void wateringPlantTest2() {
            //when
            ThrowingCallable throwingCallable = () -> wateringService.wateringPlant(
                    anotherUserId, petPlant.getId(), nowDate);

            //then
            assertThatThrownBy(throwingCallable)
                    .isInstanceOf(GreeningGlobalException.class)
                    .hasMessage(PetPlantExceptionMessage.BAD_REQUEST_PET_PLANT_WRITER.getMessage());
        }

        @Test
        @DisplayName("오늘이 아닌 날짜의 물을 줄 수 없다.")
        void wateringPlantTest3() {
            //given
            given(now.get()).willReturn(future);

            //when
            ThrowingCallable throwingCallable = () -> wateringService.wateringPlant(
                    userId, petPlant.getId(), nowDate);

            //then
            assertThatThrownBy(throwingCallable)
                    .isInstanceOf(GreeningGlobalException.class)
                    .hasMessage(PetPlantExceptionMessage.INVALID_DATE_ACCESS.getMessage());
        }
    }


}