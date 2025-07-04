package swyp.team5.greening.petPlant.controller;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import swyp.team5.greening.common.exception.GreeningGlobalException;
import swyp.team5.greening.petPlant.domain.entity.PetPlant;
import swyp.team5.greening.petPlant.domain.entity.PetPlantState;
import swyp.team5.greening.petPlant.domain.repository.DailyRecordRepository;
import swyp.team5.greening.petPlant.domain.repository.PetPlantRepository;
import swyp.team5.greening.petPlant.dto.DailyRecordContentDto;
import swyp.team5.greening.petPlant.dto.request.CreateDailyRecordRequestDto;
import swyp.team5.greening.petPlant.exception.PetPlantExceptionMessage;
import swyp.team5.greening.support.ApiTestSupport;
import swyp.team5.greening.user.domain.repository.UserRepository;

class DailyRecordControllerTest extends ApiTestSupport {

    @MockBean
    private Supplier<LocalDate> nowDate;

    @Autowired
    private PetPlantRepository petPlantRepository;

    @Autowired
    private DailyRecordRepository dailyRecordRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void init() {
        petPlantRepository.deleteAll();
        dailyRecordRepository.deleteAll();
    }

    @Nested
    @DisplayName("사용자는 애완 식물 1개를 등록한 상태이다.")
    class TestCase1 {

        PetPlant petPlant;

        CreateDailyRecordRequestDto requestDto;

        List<DailyRecordContentDto> contents;

        LocalDate now = LocalDate.of(2025, 7, 3);

        LocalDate future = LocalDate.of(2025, 7, 5);

        String title = "제목";

        @BeforeEach
        void setUp() {
            petPlant = PetPlant.builder()
                    .name("이름")
                    .plantType("민들레")
                    .state(PetPlantState.IN_PROGRESS)
                    .userId(loginUser.getId())
                    .build();
            petPlantRepository.save(petPlant);

            contents = List.of(
                    new DailyRecordContentDto("TEXT", "1번 텍스트"),
                    new DailyRecordContentDto("IMAGE", "https://example.com/1.png"),
                    new DailyRecordContentDto("TEXT", "2번 텍스트"),
                    new DailyRecordContentDto("IMAGE", "https://example.com/2.png")
            );

            requestDto = new CreateDailyRecordRequestDto(now, title, contents);
        }

        @Test
        @DisplayName("사용자는 자신이 키우는 애완 식물에 대해 오늘의 기록을 작성할 수 있다.")
        void createDailyRecord1() throws Exception {
            //given
            given(nowDate.get()).willReturn(now);

            //when
            ResultActions perform = mockMvc.perform(
                    post("/api/pet-plants/{petPlantId}/daily-record", petPlant.getId())
                            .header(HttpHeaders.AUTHORIZATION, accessToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toJson(requestDto)));

            //then
            perform.andExpectAll(
                    status().isOk(),
                    jsonPath("$.data.dailyRecordId").exists(),
                    jsonPath("$.data.dailyRecordId").isNumber()
            );
        }

        @Test
        @DisplayName("다른 사용자는 애완 식물에 대해 오늘의 기록을 작성할 수 없다.")
        void createDailyRecord2() throws Exception {
            //given
            given(nowDate.get()).willReturn(now);

            PetPlant anotherPetPlant = PetPlant.builder()
                    .name("이름")
                    .plantType("민들레")
                    .state(PetPlantState.IN_PROGRESS)
                    .userId(1000L)
                    .build();
            petPlantRepository.save(anotherPetPlant);

            //when
            ResultActions perform = mockMvc.perform(
                    post("/api/pet-plants/{petPlantId}/daily-record", anotherPetPlant.getId())
                            .header(HttpHeaders.AUTHORIZATION, accessToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toJson(requestDto)));

            //then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("$.message").value(
                            PetPlantExceptionMessage.BAD_REQUEST_PET_PLANT_WRITER.getMessage()),
                    result ->
                            assertThat(result.getResolvedException()
                                    .getClass().isAssignableFrom(GreeningGlobalException.class)));

        }

        @Test
        @DisplayName("오늘이 아닌 날짜에 대해 오늘의 기록을 작성할 수 없다.")
        void createDailyRecord3() throws Exception {
            //given
            given(nowDate.get()).willReturn(future);

            //when
            ResultActions perform = mockMvc.perform(
                    post("/api/pet-plants/{petPlantId}/daily-record", petPlant.getId())
                            .header(HttpHeaders.AUTHORIZATION, accessToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toJson(requestDto)));

            //then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("$.message").value(
                            PetPlantExceptionMessage.INVALID_DATE_ACCESS.getMessage()),
                    result ->
                            assertThat(result.getResolvedException()
                                    .getClass().isAssignableFrom(GreeningGlobalException.class)));
        }
    }

}