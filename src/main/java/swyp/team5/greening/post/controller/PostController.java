package swyp.team5.greening.post.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import swyp.team5.greening.common.dto.response.ApiResponseDto;
import swyp.team5.greening.common.resolver.LogIn;
import swyp.team5.greening.post.domain.entity.Post;
import swyp.team5.greening.post.dto.request.CreatePostRequestDto;
import swyp.team5.greening.post.dto.response.CreatePostResponseDto;
import swyp.team5.greening.post.dto.response.PostResponseDto;
import swyp.team5.greening.post.service.PostCreateService;
import swyp.team5.greening.post.service.PostDeleteService;
import swyp.team5.greening.post.service.PostReadService;

@Tag(name = "게시글 관련 API")
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostCreateService postCreateService;
    private final PostDeleteService postDeleteService;
    private final PostReadService postReadService;

    @Operation(summary = "게시글 작성 API")
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseDto<CreatePostResponseDto> createPost(
        @LogIn Long userId,
        @Validated @RequestBody CreatePostRequestDto requestDto
    ) {
        return ApiResponseDto.of(postCreateService.createPost(userId, requestDto));
    }

    @Operation(summary = "게시글 단건 조회 API")
    @GetMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseDto<PostResponseDto> getPost(@PathVariable Long postId) {
        return ApiResponseDto.of(postReadService.findPostDto(postId));
    }

    @Operation(summary = "게시글 삭제 API")
    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(
        @LogIn Long userId,
        @PathVariable Long postId
    ) {
        postDeleteService.deletePost(userId, postId);
    }

}

