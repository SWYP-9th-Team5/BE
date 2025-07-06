package swyp.team5.greening.petPlant.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import swyp.team5.greening.common.dto.response.ApiResponseDto;
import swyp.team5.greening.common.resolver.LogIn;
import swyp.team5.greening.petPlant.dto.request.CreatePetPlantRequestDto;
import swyp.team5.greening.petPlant.dto.request.FindPetPlantDateRequestDto;
import swyp.team5.greening.petPlant.dto.response.CreatePetPlantResponseDto;
import swyp.team5.greening.petPlant.dto.response.FindAllPetPlantResponseDto;
import swyp.team5.greening.petPlant.dto.response.FindPetPlantDateResponseDto;
import swyp.team5.greening.petPlant.service.PetPlantCommandService;
import swyp.team5.greening.petPlant.service.PetPlantQueryService;

@Tag(name = "애완 식물 관련 API")
@RestController
@RequestMapping("/api/pet-plants")
@RequiredArgsConstructor
public class PetPlantController {

    private final PetPlantCommandService petPlantCommandService;
    private final PetPlantQueryService petPlantQueryService;

    @Operation(summary = "애완 식물 추가 API")
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseDto<CreatePetPlantResponseDto> createPetPlant(
            @LogIn Long userId,
            @Validated @RequestBody CreatePetPlantRequestDto requestDto
    ) {
        return ApiResponseDto.of(petPlantCommandService.createPetPlant(userId, requestDto));
    }

    @Operation(summary = "나의 애완 식물 목록 조회 API")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseDto<List<FindAllPetPlantResponseDto>> getMyPetPlants(@LogIn Long userId) {
        return ApiResponseDto.of(petPlantQueryService.findMyPetPlants(userId));
    }

    @Operation(summary = "특정 애완 식물 삭제 API")
    @DeleteMapping("/{petPlantId}")
    @ResponseStatus(HttpStatus.OK)
    public void deletePetPlant(
            @LogIn Long userId,
            @PathVariable Long petPlantId
    ) {
        petPlantCommandService.deletePetPlant(userId, petPlantId);
    }

    @Operation(summary = "특정 애완 식물 특정 월 정보 조회 API")
    @GetMapping("/{petPlantId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseDto<List<FindPetPlantDateResponseDto>> getMyPetPlantCalender(
            @LogIn Long userId,
            @PathVariable Long petPlantId,
            @ModelAttribute FindPetPlantDateRequestDto requestDto
    ) {
        return ApiResponseDto.of(petPlantQueryService.findMyPetPlantCalender(
                userId, petPlantId, requestDto.year(), requestDto.month()
        ));
    }

}
