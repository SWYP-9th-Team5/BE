package swyp.team5.greening.postLike.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import swyp.team5.greening.common.dto.response.ApiResponseDto;
import swyp.team5.greening.common.resolver.LogIn;
import swyp.team5.greening.postLike.dto.PostLikeRequestDto;
import swyp.team5.greening.postLike.dto.PostLikeResponseDto;
import swyp.team5.greening.postLike.service.PostLikeCommandService;

@Tag(name = "게시글 좋아요 관련 API")
@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class PostLikeController {

    private final PostLikeCommandService postLikeCommandService;

    @Operation(summary = "좋아요/좋아요 취소 API")
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseDto<PostLikeResponseDto> likeOrCancel(
            @LogIn Long userId,
            @Validated @RequestBody PostLikeRequestDto requestDto
    ) {
        return ApiResponseDto.of(postLikeCommandService.likeOrCancel(userId, requestDto.postId()));
    }

}
