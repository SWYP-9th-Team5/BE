package swyp.team5.greening.petPlant.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import swyp.team5.greening.common.dto.response.ApiResponseDto;
import swyp.team5.greening.common.resolver.LogIn;
import swyp.team5.greening.petPlant.dto.request.CreateDailyRecordRequestDto;
import swyp.team5.greening.petPlant.dto.request.UpdateDailyRecordRequestDto;
import swyp.team5.greening.petPlant.dto.response.CreateDailyRecordResponseDto;
import swyp.team5.greening.petPlant.dto.response.FindDailyRecordResponseDto;
import swyp.team5.greening.petPlant.service.DailyRecordCommandService;
import swyp.team5.greening.petPlant.service.DailyRecordQueryService;

@Tag(name = "애완 식물 오늘의 기록 API")
@RestController
@RequestMapping("/api/pet-plants")
@RequiredArgsConstructor
public class DailyRecordController {

    private final DailyRecordCommandService dailyRecordCommandService;
    private final DailyRecordQueryService dailyRecordQueryService;

    @Operation(summary = "특정 애완 식물 오늘의 기록 작성 API")
    @PostMapping("/{petPlantId}/daily-record")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseDto<CreateDailyRecordResponseDto> createDailyRecord(
            @LogIn Long userId,
            @PathVariable Long petPlantId,
            @Validated @RequestBody CreateDailyRecordRequestDto requestDto
    ) {
        return ApiResponseDto.of(
                dailyRecordCommandService.createDailyRecord(
                        userId,
                        petPlantId,
                        requestDto
                ));
    }

    @Operation(summary = "특정 애완 식물 특정 오늘의 기록 조회 API")
    @GetMapping("/daily-record/{dailyRecordId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseDto<FindDailyRecordResponseDto> getDailyRecord(
            @LogIn Long userId,
            @PathVariable Long dailyRecordId
    ) {
        return ApiResponseDto.of(dailyRecordQueryService.findDailyRecord(
                userId,
                dailyRecordId
        ));
    }

    @Operation(summary = "특정 애완 식물 오늘의 기록 수정 API")
    @PutMapping("/daily-record/{dailyRecordId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateDailyRecord(
            @LogIn Long userId,
            @PathVariable Long dailyRecordId,
            @Validated @RequestBody UpdateDailyRecordRequestDto requestDto
    ) {
        dailyRecordCommandService.updateDailyRecord(userId, dailyRecordId, requestDto);
    }

    @Operation(summary = "특정 애완 식물 오늘의 기록 삭제 API")
    @DeleteMapping("/daily-record/{dailyRecordId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteDailyRecord(
            @LogIn Long userId,
            @PathVariable Long dailyRecordId
    ) {
        dailyRecordCommandService.deleteDailyRecord(userId, dailyRecordId);
    }

}
