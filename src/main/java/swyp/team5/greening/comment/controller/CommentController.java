package swyp.team5.greening.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import swyp.team5.greening.comment.dto.request.SaveCommentRequestDto;
import swyp.team5.greening.comment.dto.response.SaveCommentResponseDto;
import swyp.team5.greening.comment.service.CommentCommandService;
import swyp.team5.greening.common.resolver.LogIn;
import swyp.team5.greening.common.response.ApiResponseDto;

@Tag(name = "댓글 관련 API")
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentCommandService commentCommandService;

    @Operation(summary = "댓글 작성 API")
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseDto<SaveCommentResponseDto> addComment(
            @LogIn Long userId,
            @RequestBody SaveCommentRequestDto requestDto
    ) {
        return ApiResponseDto.of(commentCommandService.saveComment(userId, requestDto));
    }

}
