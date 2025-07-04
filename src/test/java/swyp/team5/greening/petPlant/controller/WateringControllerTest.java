package swyp.team5.greening.petPlant.controller;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import swyp.team5.greening.GreeningApplication;
import swyp.team5.greening.petPlant.domain.entity.PetPlant;
import swyp.team5.greening.petPlant.domain.entity.PetPlantState;
import swyp.team5.greening.petPlant.domain.entity.Watering;
import swyp.team5.greening.petPlant.domain.repository.PetPlantRepository;
import swyp.team5.greening.petPlant.domain.repository.WateringRepository;
import swyp.team5.greening.petPlant.dto.request.WateringPlantRequestDto;
import swyp.team5.greening.support.ApiTestSupport;

class WateringControllerTest extends ApiTestSupport {

    @MockBean
    private Supplier<LocalDate> nowDate;

    @Autowired
    private WateringRepository wateringRepository;

    @Autowired
    private PetPlantRepository petPlantRepository;

    @BeforeEach
    void init() {
        wateringRepository.deleteAll();
        petPlantRepository.deleteAll();
    }

    @Nested
    @DisplayName("사용자는 애완 식물을 한 개 등록해놓은 상태이다.")
    class TestCase {

        PetPlant petPlant;

        String name = "이름";
        String plantType = "민들레";

        LocalDate now = LocalDate.of(2025, 7, 3);
        LocalDate future = LocalDate.of(2025, 7, 4);

        @BeforeEach
        void setUp() {
            petPlant = PetPlant.builder()
                    .name(name)
                    .plantType(plantType)
                    .state(PetPlantState.IN_PROGRESS)
                    .userId(loginUser.getId())
                    .build();
            petPlantRepository.save(petPlant);
        }

        @Test
        @DisplayName("사용자는 자신의 애완 식물의 '물 주기 스탬프'를 등록할 수 있다.")
        void wateringPlant1() throws Exception {
            //given
            given(nowDate.get()).willReturn(now);

            //when
            ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(
                            "/api/pet-plants/{petPlantId}/watering", petPlant.getId())
                    .header(HttpHeaders.AUTHORIZATION, accessToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(new WateringPlantRequestDto(nowDate.get()))));

            List<Watering> all = wateringRepository.findAll();

            //then
            result.andExpect(status().isOk());

            assertThat(all).hasSize(1);
            assertThat(all.get(0).getPetPlantId()).isEqualTo(petPlant.getId());
            assertThat(all.get(0).getWriteDate()).isEqualTo(now);
        }

        @Test
        @DisplayName("사용자는 다른 사람의 애완 식물의 '물 주기 스탬프'를 등록할 수 없다.")
        void wateringPlant2() throws Exception{
            //given
            PetPlant anotherPetPlant = PetPlant.builder()
                    .name(name)
                    .plantType(plantType)
                    .state(PetPlantState.IN_PROGRESS)
                    .userId(1000L)
                    .build();
            petPlantRepository.save(anotherPetPlant);

            given(nowDate.get()).willReturn(now);

            //when
            ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post(
                            "/api/pet-plants/{petPlantId}/watering", anotherPetPlant.getId())
                    .header(HttpHeaders.AUTHORIZATION, accessToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(new WateringPlantRequestDto(nowDate.get()))));

            //then
            perform.andExpectAll(
                    status().isBadRequest(),
                    result ->
                            assertThat(result.getResolvedException()
                                    .getClass().isAssignableFrom(GreeningApplication.class)));

        }

        @Test
        @DisplayName("오늘이 아닌 날짜의 '물 주기 스탬프'를 등록할 수 없다.")
        void wateringPlant3() throws Exception{
            //given
            given(nowDate.get()).willReturn(now);

            //when
            ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post(
                            "/api/pet-plants/{petPlantId}/watering", petPlant.getId())
                    .header(HttpHeaders.AUTHORIZATION, accessToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(new WateringPlantRequestDto(future))));

            //then
            perform.andExpectAll(
                    status().isBadRequest(),
                    result ->
                            assertThat(result.getResolvedException()
                                    .getClass().isAssignableFrom(GreeningApplication.class)));
        }
    }
}