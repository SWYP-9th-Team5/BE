package swyp.team5.greening.petPlant.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.ResultActions;
import swyp.team5.greening.petPlant.domain.entity.DailyRecord;
import swyp.team5.greening.petPlant.domain.entity.DailyRecordState;
import swyp.team5.greening.petPlant.domain.entity.PetPlant;
import swyp.team5.greening.petPlant.domain.entity.PetPlantState;
import swyp.team5.greening.petPlant.domain.entity.Watering;
import swyp.team5.greening.petPlant.domain.repository.DailyRecordRepository;
import swyp.team5.greening.petPlant.domain.repository.PetPlantRepository;
import swyp.team5.greening.petPlant.domain.repository.WateringRepository;
import swyp.team5.greening.support.ApiTestSupport;

class PetPlantControllerTest extends ApiTestSupport {

    @Autowired
    private PetPlantRepository petPlantRepository;

    @Autowired
    private WateringRepository wateringRepository;

    @Autowired
    private DailyRecordRepository dailyRecordRepository;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @BeforeEach
    void init() {
        petPlantRepository.deleteAll();
        wateringRepository.deleteAll();
        dailyRecordRepository.deleteAll();
    }

    @Nested
    @DisplayName("사용자는 애완 식물을 1개 등록해 놓은 상태이다. 7월 3일에 물을 주었고, 7월 5일에 오늘의 일기를 작성하였다.")
    class TestCase1 {

        PetPlant petPlant;
        Watering day3Watering;
        DailyRecord day5DailyRecord;

        LocalDate day3 = LocalDate.of(2025, 7, 3);
        LocalDate day5 = LocalDate.of(2025, 7, 5);

        @BeforeEach
        void init() {
            petPlant = PetPlant.builder()
                    .name("기요미")
                    .plantType("민들레")
                    .state(PetPlantState.IN_PROGRESS)
                    .userId(loginUser.getId())
                    .build();
            petPlantRepository.save(petPlant);

            day3Watering = Watering.builder()
                    .petPlantId(petPlant.getId())
                    .writeDate(day3)
                    .build();
            wateringRepository.save(day3Watering);

            day5DailyRecord = DailyRecord.builder()
                    .title("제목")
                    .writeDate(day5)
                    .state(DailyRecordState.IN_PROGRESS)
                    .petPlantId(petPlant.getId())
                    .build();
            dailyRecordRepository.save(day5DailyRecord);
        }

        @Test
        @DisplayName("7월 3일과 7월 5일의 정보가 잘 조회된다.")
        void getMyPetPlantCalender1() throws Exception {
            //when
            ResultActions perform = mockMvc.perform(
                    get("/api/pet-plants/{petPlantId}", petPlant.getId())
                            .header(HttpHeaders.AUTHORIZATION, accessToken)
                            .param("year", "2025")
                            .param("month", "7"));

            //then
            perform.andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.size()").value(2))
                    .andExpect(jsonPath("$.data[0].date").value(day3.format(formatter)))
                    .andExpect(jsonPath("$.data[0].watering").value(true))
                    .andExpect(jsonPath("$.data[0].dailyRecordId").value(-1))
                    .andExpect(jsonPath("$.data[1].date").value(day5.format(formatter)))
                    .andExpect(jsonPath("$.data[1].watering").value(false))
                    .andExpect(jsonPath("$.data[1].dailyRecordId").value(day5DailyRecord.getId()));
        }

        @Test
        @DisplayName("다른 월로 조회할 경우, 조회되지 않는다.")
        void getMyPetPlantCalender2() throws Exception {
            //when
            ResultActions perform = mockMvc.perform(
                    get("/api/pet-plants/{petPlantId}", petPlant.getId())
                            .header(HttpHeaders.AUTHORIZATION, accessToken)
                            .param("year", "2025")
                            .param("month", "6"));

            //then
            perform.andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.size()").value(0));
        }
    }
}