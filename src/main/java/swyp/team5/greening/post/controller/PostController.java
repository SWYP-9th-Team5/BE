package swyp.team5.greening.post.controller;


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
import swyp.team5.greening.post.dto.request.CreatePostRequestDto;
import swyp.team5.greening.post.dto.response.CreatePostResponseDto;
import swyp.team5.greening.post.service.PostCreateService;

@Tag(name = "게시글 관련 API")
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostCreateService postCreateService;

    @Operation(summary = "게시글 작성 API")
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseDto<CreatePostResponseDto> createPost(
        @LogIn Long userId,
        @Validated @RequestBody CreatePostRequestDto requestDto
    ) {
        return ApiResponseDto.of(postCreateService.createPost(userId, requestDto));
    }
}
