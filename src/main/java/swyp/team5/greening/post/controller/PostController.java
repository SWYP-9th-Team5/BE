package swyp.team5.greening.post.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import swyp.team5.greening.common.dto.response.ApiResponseDto;
import swyp.team5.greening.common.resolver.LogIn;
import swyp.team5.greening.post.dto.request.CreatePostRequestDto;
import swyp.team5.greening.post.dto.response.CreatePostResponseDto;
import swyp.team5.greening.post.dto.response.PostPaginationResponseDto;
import swyp.team5.greening.post.dto.response.PostPreviewResponseDto;
import swyp.team5.greening.post.dto.response.PostResponseDto;
import swyp.team5.greening.post.service.PostCommandService;
import swyp.team5.greening.post.service.PostQueryService;


@Tag(name = "게시글 관련 API")
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostCommandService postCommandService;
    private final PostQueryService postQueryService;

    @Operation(summary = "게시글 작성 API")
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseDto<CreatePostResponseDto> createPost(
        @LogIn Long userId,
        @Validated @RequestBody CreatePostRequestDto requestDto
    ) {
        return ApiResponseDto.of(postCommandService.createPost(userId, requestDto));
    }

    @Operation(summary = "게시글 단건 조회 API")
    @GetMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseDto<PostResponseDto> getPost(@PathVariable Long postId) {
        return ApiResponseDto.of(postQueryService.findPostDto(postId));
    }

    @Operation(summary = "게시글 삭제 API")
    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(
        @LogIn Long userId,
        @PathVariable Long postId
    ) {
        postCommandService.deletePost(userId, postId);
    }

    @Operation(summary = "홈 화면 게시글 미리보기 (카테고리 별 각 6개씩)")
    @GetMapping("/home")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseDto<List<PostPreviewResponseDto>> getLatestPosts(@LogIn Long userId) {
        return ApiResponseDto.of(postQueryService.getLatestPostByCategory(userId));
    }

    @Operation(summary = "카테고리별 게시글 목록 조회 (페이징)")
    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseDto<PostPaginationResponseDto> getPostsByCategory(
        @LogIn Long userId,
        @RequestParam("category") Long categoryId,
        @RequestParam(value = "pageNumber", defaultValue = "0") int page,
        @RequestParam(value = "pageSize", defaultValue = "10") int size
    ) {
        return ApiResponseDto.of(postQueryService.getPostsByCategory(categoryId, page, size, userId));
    }

}
