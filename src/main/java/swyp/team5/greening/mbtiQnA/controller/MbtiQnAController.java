package swyp.team5.greening.mbtiQnA.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import swyp.team5.greening.common.dto.response.ApiResponseDto;
import swyp.team5.greening.mbtiQnA.dto.response.FindMbtiQuestionResponseDto;
import swyp.team5.greening.mbtiQnA.service.MbtiQnAQueryService;

@Tag(name = "MBTI 질문 및 답변 관련")
@RestController
@RequestMapping("/api/mbti-questions")
@RequiredArgsConstructor
public class MbtiQnAController {

    private final MbtiQnAQueryService mbtiQnAQueryService;

    @Operation(summary = "MBTI 질문 및 답변 목록 조회 API")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseDto<List<FindMbtiQuestionResponseDto>> getMbtiQnA() {
        return ApiResponseDto.of(mbtiQnAQueryService.findMbtiQnA());
    }

}
