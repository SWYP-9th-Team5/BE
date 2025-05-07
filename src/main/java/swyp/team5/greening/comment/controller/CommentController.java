package swyp.team5.greening.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import swyp.team5.greening.comment.dto.request.SaveCommentRequestDto;
import swyp.team5.greening.comment.dto.response.FindAllCommentResponseDto;
import swyp.team5.greening.comment.dto.response.SaveCommentResponseDto;
import swyp.team5.greening.comment.service.CommentCommandService;
import swyp.team5.greening.comment.service.CommentQueryService;
import swyp.team5.greening.common.dto.request.PaginationRequestDto;
import swyp.team5.greening.common.resolver.LogIn;
import swyp.team5.greening.common.response.ApiResponseDto;
import swyp.team5.greening.common.response.PaginationApiResponseDto;

@Tag(name = "댓글 관련 API")
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    //명령 전용 서비스
    private final CommentCommandService commentCommandService;

    //조회 전용 서비스
    private final CommentQueryService commentQueryService;

    @Operation(summary = "댓글 작성 API")
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseDto<SaveCommentResponseDto> addComment(
            @LogIn Long userId,
            @Validated @RequestBody SaveCommentRequestDto requestDto
    ) {
        return ApiResponseDto.of(commentCommandService.saveComment(userId, requestDto));
    }

    @Operation(summary = "게시물 댓글 목록 조회 API")
    @GetMapping("/posts/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public PaginationApiResponseDto<FindAllCommentResponseDto> getComment(
            @LogIn Long userId,
            @PathVariable("postId") Long postId,
            @Validated @ModelAttribute PaginationRequestDto paginationRequestDto
    ) {
        return PaginationApiResponseDto.of(commentQueryService.findAllComment(
                userId, postId,
                paginationRequestDto.pageNumber(), paginationRequestDto.pageSize()
        ));
    }

}
