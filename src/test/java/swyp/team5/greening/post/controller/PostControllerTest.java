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
import swyp.team5.greening.user.domain.entity.User;
import swyp.team5.greening.user.domain.repository.UserRepository;

@DisplayName("게시글 통합 테스트")
@AutoConfigureMockMvc
class PostControllerTest extends ApiTestSupport {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private UserRepository userRepository;

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
        void getPost() throws Exception {
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

    @DisplayName("게시글이 2개 존재한다. 로그인 유저가 다른 사람이 쓴 글을 좋아요 한 상태이다.")
    @Nested
    class TestCase3 {

        User anotherUser;
        String anotherName = "다른 유저";
        Post post;
        Post anotherPost;
        Like like;
        Like anotherLike;

        @BeforeEach
        void setUp() {
            anotherUser = User.builder()
                    .userName(anotherName)
                    .build();
            userRepository.save(anotherUser);

            post = PostFixture.getPost(loginUser.getId());
            postRepository.save(post);

            anotherPost = PostFixture.getPost(anotherUser.getId());
            postRepository.save(anotherPost);

            like = LikeFixture.getLike(anotherPost.getId(), loginUser.getId());
            likeRepository.save(like);

            anotherLike = LikeFixture.getLike(post.getId(), anotherUser.getId());
            likeRepository.save(anotherLike);
        }

        @Test
        @DisplayName("홈 화면에서 조회한 글들이 올바르게 나타난다.")
        void getLatestPosts() throws Exception {
            mockMvc.perform(get("/api/posts/home"))
                    .andExpect(jsonPath("$.data.size()").value(2))
                    .andExpect(jsonPath("$.data[0].postId").value(post.getId()))
                    .andExpect(jsonPath("$.data[0].isLike").value(false))
                    .andExpect(jsonPath("$.data[0].userId").value(loginUser.getId()))
                    .andExpect(jsonPath("$.data[1].postId").value(anotherPost.getId()))
                    .andExpect(jsonPath("$.data[1].isLike").value(false))
                    .andExpect(jsonPath("$.data[1].userId").value(anotherUser.getId()));
        }

        @Test
        @DisplayName("로그인 한 유저에 따라 좋아요 여부가 올바르게 나타난다.")
        void getLatestPosts2() throws Exception {
            mockMvc.perform(get("/api/posts/home")
                            .header(HttpHeaders.AUTHORIZATION, accessToken))
                    .andExpect(jsonPath("$.data.size()").value(2))
                    .andExpect(jsonPath("$.data[0].postId").value(post.getId()))
                    .andExpect(jsonPath("$.data[0].isLike").value(false))
                    .andExpect(jsonPath("$.data[0].userId").value(loginUser.getId()))
                    .andExpect(jsonPath("$.data[1].postId").value(anotherPost.getId()))
                    .andExpect(jsonPath("$.data[1].isLike").value(true))
                    .andExpect(jsonPath("$.data[1].userId").value(anotherUser.getId()));
        }

        @Test
        @DisplayName("1번 카테고리 글이 잘 나타난다.")
        void getPostsByCategory1() throws Exception {
            mockMvc.perform(get("/api/posts")
                            .param("category", "QnA")
                            .param("pageNumber", "1")
                            .param("pageSize", "10"))
                    .andExpect(jsonPath("$.data.size()").value(2))
                    .andExpect(jsonPath("$.data[1].postId").value(post.getId()))
                    .andExpect(jsonPath("$.data[1].isLike").value(false))
                    .andExpect(jsonPath("$.data[1].userId").value(loginUser.getId()))
                    .andExpect(jsonPath("$.data[0].postId").value(anotherPost.getId()))
                    .andExpect(jsonPath("$.data[0].isLike").value(false))
                    .andExpect(jsonPath("$.data[0].userId").value(anotherUser.getId()));
        }

        @Test
        @DisplayName("존재하지 않는 다른 카테고리 글은 조회되지 않는다.")
        void getPostsByCategory2() throws Exception {
            mockMvc.perform(get("/api/posts")
                            .param("category", "QnA")
                            .param("pageNumber", "2")
                            .param("pageSize", "10"))
                    .andExpect(jsonPath("$.data.size()").value(0));
        }

        @Test
        @DisplayName("로그인 한 유저에 따라 좋아요 여부가 올바르게 나타난다.")
        void getPostsByCategory3() throws Exception {
            mockMvc.perform(get("/api/posts")
                            .header(HttpHeaders.AUTHORIZATION, accessToken)
                            .param("category", "QnA")
                            .param("pageNumber", "1")
                            .param("pageSize", "10"))
                    .andExpect(jsonPath("$.data.size()").value(2))
                    .andExpect(jsonPath("$.data[1].postId").value(post.getId()))
                    .andExpect(jsonPath("$.data[1].isLike").value(false))
                    .andExpect(jsonPath("$.data[1].userId").value(loginUser.getId()))
                    .andExpect(jsonPath("$.data[0].postId").value(anotherPost.getId()))
                    .andExpect(jsonPath("$.data[0].isLike").value(true))
                    .andExpect(jsonPath("$.data[0].userId").value(anotherUser.getId()));
        }
    }
}