package swyp.team5.greening.petPlant.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import swyp.team5.greening.common.dto.response.ApiResponseDto;
import swyp.team5.greening.common.resolver.LogIn;
import swyp.team5.greening.petPlant.dto.request.WateringPlantRequestDto;
import swyp.team5.greening.petPlant.dto.response.WateringPlantResponseDto;
import swyp.team5.greening.petPlant.service.WateringService;

@Tag(name = "애완 식물 물 주기 스탬프 API")
@RestController
@RequestMapping("/api/pet-plants")
@RequiredArgsConstructor
public class WateringController {

    private final WateringService wateringService;

    @Operation(summary = "애완 식물 추가 API")
    @PostMapping("/{petPlantId}/watering")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseDto<WateringPlantResponseDto> wateringPlant(
            @LogIn Long userId,
            @PathVariable Long petPlantId,
            @Validated @RequestBody WateringPlantRequestDto requestDto
    ) {
        return ApiResponseDto.of(
                wateringService.wateringPlant(userId, petPlantId, requestDto.today()));
    }
}
