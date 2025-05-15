package swyp.team5.greening.mbti.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import swyp.team5.greening.common.dto.response.ApiResponseDto;
import swyp.team5.greening.mbti.dto.request.FindMbtiResultRequestDto;
import swyp.team5.greening.mbti.dto.response.FindMbtiResultResponseDto;
import swyp.team5.greening.mbti.service.MbtiResultQueryService;

@Tag(name = "Mbti 결과 관련 API")
@RestController
@RequestMapping("/api/mbti-result")
@RequiredArgsConstructor
public class MbtiResultController {

    private final MbtiResultQueryService mbtiResultQueryService;

    @Operation(summary = "Mbti 질문 결과 조회 (대답 결과로 나온 MBTI 전달 -> IST, ENF....)")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseDto<FindMbtiResultResponseDto> getMbtiResult(
            @Validated @ModelAttribute FindMbtiResultRequestDto requestDto
    ) {
        return ApiResponseDto.of(mbtiResultQueryService.findMbtiResult(requestDto.mbti()));
    }

}
