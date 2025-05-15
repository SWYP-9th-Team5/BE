package swyp.team5.greening.post.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import swyp.team5.greening.post.domain.entity.Post;
import swyp.team5.greening.post.domain.repository.PostRepository;
import swyp.team5.greening.post.dto.request.CreatePostRequestDto;
import swyp.team5.greening.post.fixture.PostFixture;
import swyp.team5.greening.postLike.domain.entity.Like;
import swyp.team5.greening.postLike.domain.repository.LikeRepository;
import swyp.team5.greening.postLike.fixture.LikeFixture;
import swyp.team5.greening.support.ApiTestSupport;

@DisplayName("게시글 통합 테스트")
@AutoConfigureMockMvc
class PostControllerTest extends ApiTestSupport {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private LikeRepository likeRepository;

    @BeforeEach
    void init() {
        postRepository.deleteAll();
        likeRepository.deleteAll();
    }

    @DisplayName("게시물이 존재하지 않는다.")
    @Nested
    class TestCase1{

        CreatePostRequestDto requestDto;

        List<CreatePostRequestDto.ContentDto> contents;

        @BeforeEach
        void setUp() {
            contents = List.of(
                    new CreatePostRequestDto.ContentDto("TEXT", "1번 텍스트"),
                    new CreatePostRequestDto.ContentDto("IMAGE", "https://example.com/1.png"),
                    new CreatePostRequestDto.ContentDto("TEXT", "2번 텍스트"),
                    new CreatePostRequestDto.ContentDto("IMAGE", "https://example.com/2.png")
            );

            requestDto = new CreatePostRequestDto("테스트 게시글", 1L, contents);

        }

        @Test
        @DisplayName("사용자는 게시글을 작성할 수 있다.")
        void createPost() throws Exception {
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

    @DisplayName("게시글이 1개 존재한다. 로그인 유저가 게시글을 작성하였고, 좋아요 한 상태이다.")
    @Nested
    class TestCase2 {

        Post post;
        Like like;

        @BeforeEach
        void setUp() {
            post = PostFixture.getPost(loginUser.getId());
            postRepository.save(post);
            like = LikeFixture.getLike(post.getId(), loginUser.getId());
            likeRepository.save(like);
        }

        @Test
        @DisplayName("사용자는 게시글을 단건 조회할 수 있다.")
        void getSinglePost() throws Exception {
            // when
            ResultActions result = mockMvc.perform(get("/api/posts/{id}", post.getId())
                    .header(HttpHeaders.AUTHORIZATION, accessToken));

            // then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.postId").value(post.getId()))
                    .andExpect(jsonPath("$.data.title").value(post.getTitle()))
                    .andExpect(jsonPath("$.data.content.size()").value(5))
                    .andExpect(jsonPath("$.data.isLike").value(true))
                    .andExpect(jsonPath("$.data.isAuthor").value(true));
        }

        @Test
        @DisplayName("사용자는 게시글을 삭제할 수 있다.")
        void deletePost() throws Exception {
            // when
            ResultActions result = mockMvc.perform(delete("/api/posts/{id}", post.getId())
                    .header(HttpHeaders.AUTHORIZATION, accessToken));

            // then
            result.andExpect(status().isNoContent());
        }
    }
}