package swyp.team5.greening.post.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        List<CreatePostRequestDto.ContentDto> contents = List.of(
            new CreatePostRequestDto.ContentDto("TEXT", "1번 텍스트"),
            new CreatePostRequestDto.ContentDto("IMAGE", "https://example.com/1.png"),
            new CreatePostRequestDto.ContentDto("TEXT", "2번 텍스트"),
            new CreatePostRequestDto.ContentDto("IMAGE", "https://example.com/2.png")
        );
        CreatePostRequestDto requestDto = new CreatePostRequestDto("테스트 게시글", 1L, contents);

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
}