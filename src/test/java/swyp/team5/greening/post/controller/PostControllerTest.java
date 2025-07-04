package swyp.team5.greening.post.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import swyp.team5.greening.auth.infrastructure.JwtTokenProvider;
import swyp.team5.greening.common.exception.GreeningGlobalException;
import swyp.team5.greening.post.domain.entity.Post;
import swyp.team5.greening.post.domain.entity.PostState;
import swyp.team5.greening.post.domain.repository.PostRepository;
import swyp.team5.greening.post.dto.PostContentDto;
import swyp.team5.greening.post.dto.request.CreatePostRequestDto;
import swyp.team5.greening.post.dto.request.UpdatePostRequestDto;
import swyp.team5.greening.post.fixture.PostFixture;
import swyp.team5.greening.postLike.domain.entity.Like;
import swyp.team5.greening.postLike.domain.repository.LikeRepository;
import swyp.team5.greening.postLike.fixture.LikeFixture;
import swyp.team5.greening.support.ApiTestSupport;
import swyp.team5.greening.user.domain.entity.LoginType;
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
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void init() {
        postRepository.deleteAll();
        likeRepository.deleteAll();
    }

    @DisplayName("게시물이 존재하지 않는다.")
    @Nested
    class TestCase1 {

        CreatePostRequestDto requestDto;

        List<PostContentDto> contents;

        @BeforeEach
        void setUp() {
            contents = List.of(
                    new PostContentDto("TEXT", "1번 텍스트"),
                    new PostContentDto("IMAGE", "https://example.com/1.png"),
                    new PostContentDto("TEXT", "2번 텍스트"),
                    new PostContentDto("IMAGE", "https://example.com/2.png")
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
            ResultActions result = mockMvc.perform(get("/api/posts/{postId}", post.getId())
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
        @DisplayName("사용자는 자신이 작성한 게시글을 조회할 수 있다.")
        void getMyPost() throws Exception {
            // when
            ResultActions result = mockMvc.perform(get("/api/posts/my")
                    .header(HttpHeaders.AUTHORIZATION, accessToken)
                    .param("pageNumber", "1")
                    .param("pageSize", "10"));

            // then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.data[0].postId").value(post.getId()))
                    .andExpect(jsonPath("$.data[0].title").value(post.getTitle()))
                    .andExpect(jsonPath("$.data[0].isLike").value(true));
        }

        @Test
        @DisplayName("사용자는 자신이 작성한 게시글을 수정할 수 있다.")
        void updatePost() throws Exception {
            // given
            String updateTitle = "수정된 제목";
            List<PostContentDto> updateContents = List.of(new PostContentDto("TEXT", "수정된 내용"));

            UpdatePostRequestDto requestDto = new UpdatePostRequestDto(updateTitle, updateContents);

            // when
            ResultActions result = mockMvc.perform(put("/api/posts/{postId}", post.getId())
                    .header(HttpHeaders.AUTHORIZATION, accessToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(requestDto)));

            Post resultPost = postRepository.findByIdAndState(post.getId(), PostState.IN_PROGRESS)
                    .orElseThrow();

            // then
            result.andExpect(status().isOk());

            assertThat(resultPost.getTitle()).isEqualTo(updateTitle);
        }

        @Test
        @DisplayName("자신이 작성하지 않은 게시글을 수정하려 할 경우 예외가 발생한다.")
        void updatePost2() throws Exception {
            // given
            User anotherUser = User.builder()
                    .email("email")
                    .loginType(LoginType.KAKAO)
                    .userName("name")
                    .nickname("nickname")
                    .build();

            userRepository.save(anotherUser);
            String anotherUserToken = jwtTokenProvider.createToken(anotherUser.getId());

            String updateTitle = "수정된 제목";
            List<PostContentDto> updateContents = List.of(new PostContentDto("TEXT", "수정된 내용"));

            UpdatePostRequestDto requestDto = new UpdatePostRequestDto(updateTitle, updateContents);

            // when
            ResultActions perform = mockMvc.perform(put("/api/posts/{postId}", post.getId())
                    .header(HttpHeaders.AUTHORIZATION, anotherUserToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(requestDto)));

            // then
            perform.andExpect(result ->
                    assertThat(result.getResolvedException().getClass())
                            .isAssignableFrom(GreeningGlobalException.class));
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