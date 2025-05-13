package swyp.team5.greening.post.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import swyp.team5.greening.post.domain.entity.Post;
import swyp.team5.greening.post.domain.repository.PostRepository;
import swyp.team5.greening.post.dto.request.CreatePostRequestDto;
import swyp.team5.greening.support.ApiTestSupport;
import swyp.team5.greening.user.infrastructure.GoogleLoginClient;

@DisplayName("게시글 통합 테스트")
@AutoConfigureMockMvc
class PostControllerTest extends ApiTestSupport {

    @Autowired
    private PostRepository postRepository;

    @MockBean
    private GoogleLoginClient googleLoginClient;

    @BeforeEach
    void init() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("사용자는 게시글을 작성할 수 있다.")
    void createPost() throws Exception {
        // given
        CreatePostRequestDto requestDto = getSamplePostDto();

        // when
        ResultActions result = mockMvc.perform(post("/api/posts")
            .header(HttpHeaders.AUTHORIZATION, accessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(toJson(requestDto)));

        // then
        result.andExpect(status().isOk())
            .andExpect(jsonPath("$.data.postId").exists())
            .andExpect(jsonPath("$.data.postId").isNumber());
    }

    @Test
    @DisplayName("사용자는 게시글을 단건 조회할 수 있다.")
    void getSinglePost() throws Exception {
        // given
        Post savedPost = savePost(); // 직접 저장 또는 service 활용

        // when
        ResultActions result = mockMvc.perform(get("/api/posts/{id}", savedPost.getId())
            .header(HttpHeaders.AUTHORIZATION, accessToken));

        // then
        result.andExpect(status().isOk())
            .andExpect(jsonPath("$.data.postId").value(savedPost.getId()))
            .andExpect(jsonPath("$.data.title").value("테스트 게시글"));
    }

    @Test
    @DisplayName("사용자는 게시글을 삭제할 수 있다.")
    void deletePost() throws Exception {
        // given
        Post savedPost = savePost();

        // when
        ResultActions result = mockMvc.perform(delete("/api/posts/{id}", savedPost.getId())
            .header(HttpHeaders.AUTHORIZATION, accessToken));

        // then
        result.andExpect(status().isNoContent());
    }

    private CreatePostRequestDto getSamplePostDto() {
        List<CreatePostRequestDto.ContentDto> contents = List.of(
            new CreatePostRequestDto.ContentDto("TEXT", "1번 텍스트"),
            new CreatePostRequestDto.ContentDto("IMAGE", "https://example.com/1.png"),
            new CreatePostRequestDto.ContentDto("TEXT", "2번 텍스트"),
            new CreatePostRequestDto.ContentDto("IMAGE", "https://example.com/2.png")
        );
        return new CreatePostRequestDto("테스트 게시글", 1L, contents);
    }

    private Post savePost() {
        CreatePostRequestDto dto = getSamplePostDto();
        return postRepository.save(
            Post.builder()
                .title(dto.title())
                .userId(1L)
                .categoryId(dto.categoryId())
                .likeCount(0L)
                .commentCount(0L)
                .state(swyp.team5.greening.post.domain.entity.PostState.IN_PROGRESS)
                .build()
        );
    }
}